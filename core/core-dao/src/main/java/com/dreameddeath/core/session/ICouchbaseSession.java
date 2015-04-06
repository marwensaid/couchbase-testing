/*
 * Copyright Christophe Jeunesse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dreameddeath.core.session;

import com.dreameddeath.core.exception.DuplicateUniqueKeyException;
import com.dreameddeath.core.exception.dao.DaoException;
import com.dreameddeath.core.exception.storage.StorageException;
import com.dreameddeath.core.exception.validation.ValidationException;
import com.dreameddeath.core.model.document.CouchbaseDocument;
import com.dreameddeath.core.model.unique.CouchbaseUniqueKey;
import com.dreameddeath.core.model.view.IViewAsyncQueryResult;
import com.dreameddeath.core.model.view.IViewQuery;
import com.dreameddeath.core.model.view.IViewQueryResult;
import org.joda.time.DateTime;
import rx.Observable;

/**
 * Created by ceaj8230 on 20/11/2014.
 */
public interface ICouchbaseSession {
    public long getCounter(String key) throws DaoException,StorageException;
    public long incrCounter(String key, long byVal) throws DaoException,StorageException;
    public long decrCounter(String key, long byVal) throws DaoException,StorageException;

    public CouchbaseDocument get(String key) throws DaoException,StorageException;
    public <T extends CouchbaseDocument> T get(String key, Class<T> targetClass) throws DaoException,StorageException;
    public <T extends CouchbaseDocument> T getFromUID(String uid, Class<T> targetClass) throws DaoException,StorageException;
    public <T extends CouchbaseDocument> String getKeyFromUID(String uid, Class<T> targetClass) throws DaoException;

    public <T extends CouchbaseDocument> T newEntity(Class<T> clazz);
    public <T extends CouchbaseDocument> T create(T obj) throws ValidationException,DaoException,StorageException;
    public <T extends CouchbaseDocument> T buildKey(T obj) throws DaoException,StorageException;
    public <T extends CouchbaseDocument> T save(T obj) throws ValidationException,DaoException,StorageException;

    public <T extends CouchbaseDocument> T update(T obj) throws ValidationException,DaoException,StorageException;
    public <T extends CouchbaseDocument> T delete(T obj) throws ValidationException,DaoException,StorageException;

    public void validate(CouchbaseDocument doc) throws ValidationException;

    public CouchbaseUniqueKey getUniqueKey(String internalKey) throws DaoException,StorageException;
    public void addOrUpdateUniqueKey(CouchbaseDocument doc, Object value, String nameSpace) throws ValidationException,DaoException,StorageException,DuplicateUniqueKeyException;
    public void removeUniqueKey(String internalKey) throws DaoException,StorageException,ValidationException;

    public DateTime getCurrentDate();

    public <T extends CouchbaseDocument> IViewQuery initViewQuery(Class<T> forClass,String viewName) throws DaoException;
    public IViewQueryResult executeQuery(IViewQuery query) throws DaoException,StorageException;
    public Observable<IViewAsyncQueryResult> executeAsyncQuery(IViewQuery query) throws DaoException,StorageException;

    public void reset(); //Clean cache
}
