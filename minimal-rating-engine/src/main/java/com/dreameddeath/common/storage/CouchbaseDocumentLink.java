package com.dreameddeath.common.storage;

import java.util.HashSet;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.spy.memcached.transcoders.Transcoder;

public abstract class CouchbaseDocumentLink<T extends CouchbaseDocument>{
    private String _key;
    private T      _docObject;
    
    @JsonIgnore
    public abstract Transcoder<T> getTranscoder();
    
    @JsonProperty("key")
    public final String getKey(){ return _key;}
    public final void setKey(String key){ this._key = key; }
    
    @JsonIgnore
    public T getLinkedObject(){ return _docObject; }
    public void setLinkedObject(T docObj){ _docObject=docObj; }
    
    
    @Override
    public String toString(){
        return "key : "+getKey();
    }
}
