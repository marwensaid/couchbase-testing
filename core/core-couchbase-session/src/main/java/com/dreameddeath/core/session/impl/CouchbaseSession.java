/*
 * Copyright Christophe Jeunesse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dreameddeath.core.session.impl;

import com.dreameddeath.core.couchbase.exception.StorageException;
import com.dreameddeath.core.dao.counter.CouchbaseCounterDao;
import com.dreameddeath.core.dao.document.CouchbaseDocumentDao;
import com.dreameddeath.core.dao.document.IDaoForDocumentWithUID;
import com.dreameddeath.core.dao.exception.DaoException;
import com.dreameddeath.core.dao.exception.ReadOnlyException;
import com.dreameddeath.core.dao.exception.validation.ValidationException;
import com.dreameddeath.core.dao.factory.CouchbaseCounterDaoFactory;
import com.dreameddeath.core.dao.factory.CouchbaseDocumentDaoFactory;
import com.dreameddeath.core.dao.model.view.IViewAsyncQueryResult;
import com.dreameddeath.core.dao.model.view.IViewQuery;
import com.dreameddeath.core.dao.model.view.IViewQueryResult;
import com.dreameddeath.core.dao.session.ICouchbaseSession;
import com.dreameddeath.core.dao.unique.CouchbaseUniqueKeyDao;
import com.dreameddeath.core.dao.view.CouchbaseViewDao;
import com.dreameddeath.core.date.IDateTimeService;
import com.dreameddeath.core.model.document.CouchbaseDocument;
import com.dreameddeath.core.model.exception.DuplicateUniqueKeyException;
import com.dreameddeath.core.model.unique.CouchbaseUniqueKey;
import com.dreameddeath.core.user.IUser;
import com.dreameddeath.core.validation.Validator;
import com.dreameddeath.core.validation.ValidatorContext;
import org.joda.time.DateTime;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

public class CouchbaseSession implements ICouchbaseSession {
    final private CouchbaseSessionFactory sessionFactory;
    final private String keyPrefix;
    final private SessionType sessionType;
    final private IDateTimeService dateTimeService;
    final private IUser user;

    private Map<String,CouchbaseDocument> sessionCache = new HashMap<>();
    private Map<String,CouchbaseUniqueKey> keyCache = new HashMap<>();
    private Map<String,Long> counters = new HashMap<>();

    public CouchbaseSession(CouchbaseSessionFactory factory, IUser user){
        this(factory, SessionType.READ_ONLY,user);
    }

    public CouchbaseSession(CouchbaseSessionFactory factory, IUser user,String keyPrefix){
        this(factory, SessionType.READ_ONLY,user,keyPrefix);
    }

    public CouchbaseSession(CouchbaseSessionFactory factory, SessionType type, IUser user){
        this(factory, type, user,null);
    }

    public CouchbaseSession(CouchbaseSessionFactory factory, SessionType type, IUser user,String keyPrefix) {
        sessionFactory = factory;
        dateTimeService = factory.getDateTimeServiceFactory().getService();
        sessionType = type;
        this.user = user;
        this.keyPrefix = keyPrefix;
    }
    protected CouchbaseDocumentDaoFactory getDocumentFactory(){
        return sessionFactory.getDocumentDaoFactory();
    }

    protected CouchbaseCounterDaoFactory getCounterDaoFactory(){
        return sessionFactory.getCounterDaoFactory();
    }

    @Override
    public void reset(){
        sessionCache.clear();
        counters.clear();
    }

    @Override
    public String getKeyPrefix() {
        return keyPrefix;
    }

    public boolean isCalcOnly(){
        return sessionType== SessionType.CALC_ONLY;
    }
    public boolean isReadOnly(){
        return sessionType== SessionType.READ_ONLY;
    }
    protected void checkReadOnly(CouchbaseDocument doc) throws ReadOnlyException{
        if(isReadOnly()){
            throw new ReadOnlyException(doc);
        }
    }

    protected void checkReadOnly(Class docClass) throws ReadOnlyException{
        if(isReadOnly()){
            throw new ReadOnlyException(docClass);
        }
    }

    protected void checkReadOnly(String counterKey) throws ReadOnlyException{
        if(isReadOnly()){
            throw new ReadOnlyException(counterKey);
        }
    }

    @Override
    public long getCounter(String key) throws DaoException,StorageException {
        CouchbaseCounterDao dao = sessionFactory.getCounterDaoFactory().getDaoForKey(key);
        if(isCalcOnly() && counters.containsKey(key)){
            return counters.get(key);
        }
        Long value = dao.getCounter(this,key,isCalcOnly());
        if(isCalcOnly()){
            counters.put(key,value);
        }
        return value;
    }

    @Override
    public long incrCounter(String key, long byVal) throws DaoException,StorageException {
        checkReadOnly(key);
        if(isCalcOnly()){
            Long result = getCounter(key);
            result+=byVal;
            counters.put(key,result);
            return result;
        }
        else{
            CouchbaseCounterDao dao = sessionFactory.getCounterDaoFactory().getDaoForKey(key);
            return dao.incrCounter(this,key, byVal, isCalcOnly());
        }
    }

    @Override
    public long decrCounter(String key, long byVal) throws DaoException,StorageException {
        checkReadOnly(key);
        if(isCalcOnly()){
            Long result = getCounter(key);
            result-=byVal;
            counters.put(key,result);
            return result;
        }
        else{
            CouchbaseCounterDao dao = sessionFactory.getCounterDaoFactory().getDaoForKey(key);
            return dao.decrCounter(this,key, byVal, isCalcOnly());
        }
    }

    
    public void attachDocument(CouchbaseDocument doc){
        if(doc.getBaseMeta().getKey()!=null){
            sessionCache.put(doc.getBaseMeta().getKey(),doc);
        }
    }
    

    @Override
    public <T extends CouchbaseDocument> T newEntity(Class<T> clazz){
        try{
            return attachEntity(clazz.newInstance());
        }
        catch(Exception e){
            ///TODO log something
            return null;
        }
    }


    @Override
    public <T extends CouchbaseDocument> T attachEntity(T entity){
        try{
            attachDocument(entity);
            return entity;
        }
        catch(Exception e){
            ///TODO log something
            return null;
        }
    }



    @Override
    public <T extends CouchbaseDocument> T create(T obj) throws ValidationException,DaoException,StorageException {
        checkReadOnly(obj);
        CouchbaseDocumentDao<T> dao = (CouchbaseDocumentDao<T>) sessionFactory.getDocumentDaoFactory().getDaoForClass(obj.getClass());
        dao.create(this,obj,isCalcOnly());
        attachDocument(obj);
        return obj;
    }

    @Override
    public <T extends CouchbaseDocument> T buildKey(T obj) throws DaoException,StorageException {
        if(obj.getBaseMeta().getState()== CouchbaseDocument.DocumentState.NEW){
            ((CouchbaseDocumentDao<T>) sessionFactory.getDocumentDaoFactory().getDaoForClass(obj.getClass())).buildKey(this, obj);
        }
        return obj;
    }

    @Override
    public CouchbaseDocument get(String key) throws DaoException,StorageException {
        CouchbaseDocument result = sessionCache.get(key);
        if(result==null){
            CouchbaseDocumentDao dao = sessionFactory.getDocumentDaoFactory().getDaoForKey(key);
            result = dao.get(this,key);
            attachDocument(result);
        }
        return result;
    }

    @Override
    public <T extends CouchbaseDocument> T get(String key, Class<T> targetClass) throws DaoException,StorageException {
        CouchbaseDocument cacheResult = sessionCache.get(key);
        if(cacheResult !=null){
            return (T)cacheResult;
        }
        else{
            CouchbaseDocumentDao<T> dao = sessionFactory.getDocumentDaoFactory().getDaoForClass(targetClass);
            T result = dao.get(this,key);
            attachDocument(result);
            return result;
        }
    }

    @Override
    public <T extends CouchbaseDocument> T update(T obj)throws ValidationException,DaoException,StorageException {
        checkReadOnly(obj);
        CouchbaseDocumentDao<T> dao = sessionFactory.getDocumentDaoFactory().getDaoForClass((Class<T>) obj.getClass());
        dao.update(this, obj, isCalcOnly());
        return obj;
    }

    @Override
    public <T extends CouchbaseDocument> T delete(T obj)throws ValidationException,DaoException,StorageException {
        checkReadOnly(obj);
        CouchbaseDocumentDao<T> dao = sessionFactory.getDocumentDaoFactory().getDaoForClass((Class<T>) obj.getClass());
        dao.delete(this, obj, isCalcOnly());
        return obj;
    }

    @Override
    public void validate(CouchbaseDocument doc) throws ValidationException {
        ((Validator<CouchbaseDocument>)sessionFactory.getValidatorFactory().getValidator(doc.getClass())).validate(ValidatorContext.buildContext(this),doc);
    }

    @Override
    public <T extends CouchbaseDocument> T getFromUID(String uid, Class<T> targetClass) throws DaoException,StorageException {
        IDaoForDocumentWithUID dao = (IDaoForDocumentWithUID) sessionFactory.getDocumentDaoFactory().getDaoForClass(targetClass);
        return get(dao.getKeyFromUID(uid), targetClass);
    }


    @Override
    public <T extends CouchbaseDocument> String getKeyFromUID(String uid, Class<T> targetClass) throws DaoException{
        IDaoForDocumentWithUID dao = (IDaoForDocumentWithUID) sessionFactory.getDocumentDaoFactory().getDaoForClass(targetClass);
        return dao.getKeyFromUID(uid);
    }


    @Override
    public <T extends CouchbaseDocument> T save(T obj) throws ValidationException,DaoException,StorageException {
        if(obj.getBaseMeta().getState().equals(CouchbaseDocument.DocumentState.NEW)){
            return create(obj);
        }
        else if(obj.getBaseMeta().getState().equals(CouchbaseDocument.DocumentState.DELETED)){
            return delete(obj);
        }
        else{
            return update(obj);
        }
    }

    @Override
    public void addOrUpdateUniqueKey(CouchbaseDocument doc, Object value, String nameSpace)throws ValidationException,DaoException,StorageException,DuplicateUniqueKeyException{
        //Skip null value
        if(value==null){
            return;
        }
        checkReadOnly(doc);
        CouchbaseUniqueKeyDao dao = sessionFactory.getUniqueKeyDaoFactory().getDaoFor(nameSpace);
        CouchbaseUniqueKey keyDoc =dao.addOrUpdateUniqueKey(this, nameSpace, value.toString(), doc, isCalcOnly());
        keyCache.put(keyDoc.getBaseMeta().getKey(),keyDoc);
    }



    @Override
    public CouchbaseUniqueKey getUniqueKey(String internalKey)throws DaoException,StorageException {
        CouchbaseUniqueKey keyDoc =keyCache.get(sessionFactory.getUniqueKeyDaoFactory().getDaoForInternalKey(internalKey).buildKey(internalKey));
        if(keyDoc==null){
            CouchbaseUniqueKeyDao dao = sessionFactory.getUniqueKeyDaoFactory().getDaoForInternalKey(internalKey);
            return dao.getFromInternalKey(this,internalKey);
        }
        return keyDoc;
    }


    @Override
    public void removeUniqueKey(String internalKey) throws DaoException,StorageException,ValidationException {
        CouchbaseUniqueKey obj = getUniqueKey(internalKey);
        checkReadOnly(obj);
        CouchbaseUniqueKeyDao dao = sessionFactory.getUniqueKeyDaoFactory().getDaoForInternalKey(internalKey);
        dao.removeUniqueKey(this,obj,internalKey,isCalcOnly());
        if(obj.getBaseMeta().getState().equals(CouchbaseDocument.DocumentState.DELETED)){
            keyCache.remove(obj.getBaseMeta().getKey());
        }
    }

    @Override
    public DateTime getCurrentDate() {
        return null;
    }


    public enum SessionType{
        READ_ONLY,
        CALC_ONLY,
        READ_WRITE
    }

    public IDateTimeService getDateTimeService(){ return dateTimeService; }

    @Override
    public <T extends CouchbaseDocument> IViewQuery initViewQuery(Class<T> forClass,String viewName) throws DaoException{
        CouchbaseViewDao viewDao = sessionFactory.getDocumentDaoFactory().getViewDaoFactory().getViewDaoFor(forClass,viewName);
        return viewDao.buildViewQuery(keyPrefix);
    }

    @Override
    public IViewQueryResult executeQuery(IViewQuery query){
        return query.getDao().query(this,isCalcOnly(),query);
    }

    @Override
    public Observable<IViewAsyncQueryResult> executeAsyncQuery(IViewQuery query) throws DaoException,StorageException {
        return query.getDao().asyncQuery(this,isCalcOnly(),query);
    }
}
