package com.dreameddeath.core.dao;

import com.dreameddeath.core.dao.validation.Validator;
import com.dreameddeath.core.exception.dao.ValidationException;
import com.dreameddeath.core.model.document.CouchbaseDocument;
import com.dreameddeath.core.model.document.CouchbaseDocumentLink;
import com.dreameddeath.core.storage.CouchbaseClientWrapper;
import com.dreameddeath.core.storage.OperationFutureWrapper;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.transcoders.Transcoder;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public abstract class CouchbaseDocumentDao<T extends CouchbaseDocument>{
    private CouchbaseDocumentDaoFactory _parentFactory;
    private CouchbaseClientWrapper _client;
    public abstract Transcoder<T> getTranscoder();
    protected abstract void buildKey(T newObject);
    public abstract String getKeyPattern();

    public CouchbaseClientWrapper getClientWrapper(){
        return _client;
    }
    
    public CouchbaseDocumentDaoFactory getDaoFactory(){
        return _parentFactory;
    }
    
    //May be overriden to improve (bulk key attribution)
    protected void buildKeys(Collection<T> newObjects){
        for(T newObject:newObjects){
            if(newObject.getKey()==null){
                buildKey(newObject);
            }
        }
    }

    protected  void updateRevision(T obj){
        obj.incDocRevision();
        obj.updateDocLastModDate();
    }

    protected void updateRevision(Collection<T> objs){
        for(T obj:objs){
            updateRevision(obj);
        }
    }
    
    //Maybe overriden to improve (bulk key attribution)
    public void buildKeysForLinks(Collection<? extends CouchbaseDocumentLink<? extends T>> links){
        for(CouchbaseDocumentLink<? extends T> link:links){
            if(link.getKey()!=null){
                continue;
            }
            buildKey(link.getLinkedObject());
        }
    }
    
    
    public CouchbaseDocumentDao(CouchbaseClientWrapper client,CouchbaseDocumentDaoFactory factory){
        _client = client;
        _parentFactory = factory;
    }

    public void validate(T obj) throws ValidationException{
        Validator<T> validator = getDaoFactory().getValidatorFactory().getValidator(obj);
        if(validator!=null){
            validator.validate(obj,null);
        }
    }

    public T create(T obj) throws ValidationException{
        validate(obj);
        if(!obj.getState().equals(CouchbaseDocument.State.NEW)){
            /**TODO throw an error*/
        }
        if(obj.getKey()==null){
            buildKey(obj);
        }
        
        try {
            _client.add(obj,getTranscoder()).get();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted waiting for cas update", e);
        } catch (ExecutionException e) {
            if(e.getCause() instanceof CancellationException) {
                throw (CancellationException) e.getCause();
            } else {
                throw new RuntimeException("Exception waiting for cas update", e);
            }
        }
        obj.setStateSync();
        return obj;
    }
    
    public Collection<T> createBulk(Collection<T> objs) throws ValidationException{
        List<OperationFutureWrapper<Boolean,T>> futures = new ArrayList<OperationFutureWrapper<Boolean,T>>(objs.size());
        for(T obj : objs){
            if(!obj.getState().equals(CouchbaseDocument.State.NEW)){
                /**TODO throw an error*/
            }
            validate(obj);
        }
        
        buildKeys(objs);
        updateRevision(objs);
        for(T obj:objs){
            futures.add(_client.add(obj,getTranscoder()));
        }
        List<RuntimeException> exceptions = new ArrayList<RuntimeException>();
        for(OperationFutureWrapper<Boolean,T> future:futures){
            try{
                Boolean result = future.get();
                if(!result){
                    future.getDoc().setKey(null);
                    ///TODO better error management for errors
                }
                else{
                    future.getDoc().setStateSync();
                }
            }
            catch (InterruptedException e) {
                exceptions.add(new RuntimeException("Interrupted waiting for value", e));
            }
            catch (ExecutionException e) {
                if(e.getCause() instanceof CancellationException) {
                    exceptions.add((CancellationException) e.getCause());
                } else {
                    exceptions.add(new RuntimeException("Exception waiting for value", e));
                }
            }
        }
        return objs;
    }

    public T get(String key){
        T result=_client.gets(key,getTranscoder());
        result.setStateSync();
        return result;
    }
    
    public List<T> getBulk(Set<String> keys){
        List<OperationFutureWrapper<CASValue<T>,T>> futures = new ArrayList<OperationFutureWrapper<CASValue<T>,T>>(keys.size());
        List<T> results = new ArrayList<T>(keys.size());
        for(String key : keys){
            futures.add(_client.asyncGets(key,getTranscoder()));
        }
        List<RuntimeException> exceptions = new ArrayList<RuntimeException>();
        for(OperationFutureWrapper<CASValue<T>,T> future : futures){
            try{
                T result = future.get().getValue();
                result.setStateSync();
                results.add(result);
            }
            catch (InterruptedException e) {
                exceptions.add(new RuntimeException("Interrupted waiting for value", e));
            }
            catch (ExecutionException e) {
                if(e.getCause() instanceof CancellationException) {
                    exceptions.add((CancellationException) e.getCause());
                } else {
                    exceptions.add(new RuntimeException("Exception waiting for value", e));
                }
            }
        }
        return results;
    }
    
    public T update(T obj) throws ValidationException{
        validate(obj);
        if(obj.getKey()==null){/**TODO throw an error*/}
        updateRevision(obj);
        _client.cas(obj,getTranscoder());
        obj.setStateSync();
        return obj;
    }
    
    public Collection<T> updateBulk(Collection<T> objs) throws ValidationException{
        List<OperationFutureWrapper<CASResponse,T>> futures = new ArrayList<OperationFutureWrapper<CASResponse,T>>(objs.size());

        for(T obj:objs){
            validate(obj);
        }
        buildKeys(objs);
        updateRevision(objs);
        for(T obj:objs){

            futures.add(_client.asyncCas(obj,getTranscoder()));
        }
        
        List<RuntimeException> exceptions = new ArrayList<RuntimeException>();
        for(OperationFutureWrapper<CASResponse,T> future:futures){
            try{
                
                CASResponse result = future.get();
                if(result.equals(CASResponse.OK)){
                    future.getDoc().setStateSync();
                }
                else{
                    ///TODO manage errors
                }
            }
            catch (InterruptedException e) {
                exceptions.add(new RuntimeException("Interrupted waiting for value", e));
            }
            catch (ExecutionException e) {
                if(e.getCause() instanceof CancellationException) {
                    exceptions.add((CancellationException) e.getCause());
                } else {
                    exceptions.add(new RuntimeException("Exception waiting for value", e));
                }
            }
        }
        return objs;
    }
    
    public Collection<T> getLinkObjBulk(Collection<CouchbaseDocumentLink<T>> links){
        Collection<T> results = new ArrayList<T>(links.size());
        
        Map<String,List<CouchbaseDocumentLink<T>>> linkedDocs = new HashMap<String,List<CouchbaseDocumentLink<T>>>(links.size());
        //Retrive Unique Key
        for(CouchbaseDocumentLink<T> link : links){
            if(link.getLinkedObject()!=null){ results.add(link.getLinkedObject()); continue; }
            if(linkedDocs.containsKey(link.getKey())){
                linkedDocs.get(link.getKey()).add(link);
            }
            else{
                List<CouchbaseDocumentLink<T>> linksPerKeyList = new ArrayList<CouchbaseDocumentLink<T>>();
                linksPerKeyList.add(link);
                linkedDocs.put(link.getKey(),linksPerKeyList);
            }
        }
        Collection<T> objs = getBulk(linkedDocs.keySet());
        results.addAll(objs);
        for(T obj:objs){
            for(CouchbaseDocumentLink<T> link: linkedDocs.get(obj.getKey())){
                link.setLinkedObject(obj);
            }
        }
        return results;
    }
    
    public T getLinkObj(CouchbaseDocumentLink<T> link){
        if(link.getLinkedObject()!=null) { return link.getLinkedObject(); }
        T result = get(link.getKey());
        link.setLinkedObject(result);
        
        return result;
    }
    
}