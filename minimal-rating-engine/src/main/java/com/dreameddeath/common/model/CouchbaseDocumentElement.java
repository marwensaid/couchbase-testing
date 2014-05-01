package com.dreameddeath.common.model;

import java.util.HashSet;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
@JsonAutoDetect(getterVisibility=Visibility.NONE,fieldVisibility=Visibility.NONE)
public abstract class CouchbaseDocumentElement{
    private ImmutableProperty<CouchbaseDocumentElement> _parentElt=new ImmutableProperty<CouchbaseDocumentElement>(null);
    private List<CouchbaseDocumentElement> _childElementList=new ArrayList<CouchbaseDocumentElement>();
    
    protected void addChildElement(CouchbaseDocumentElement elt){
        System.out.println("Adding child element "+elt);
        _childElementList.add(elt);
    }
    
    public <T extends CouchbaseDocumentElement> List<T> getChildElementsOfType(Class<T> clazz){
        List<T> res=new ArrayList<T>();
        for(CouchbaseDocumentElement child : _childElementList){
            System.out.println("Checking child element "+child);
            if(clazz.isAssignableFrom(child.getClass())){
                res.add((T)child);
            }
            res.addAll(child.getChildElementsOfType(clazz));
        }
        return res;
    }
    
    public CouchbaseDocumentElement getParentElement() { return _parentElt.get();}
    public void setParentElement(CouchbaseDocumentElement parentElt) { 
        _parentElt.set(parentElt);
        parentElt.addChildElement(this);
    }
    
    public CouchbaseDocument getParentDocument() { 
        if(this instanceof CouchbaseDocument){
            return (CouchbaseDocument)this;
        }
        else if(_parentElt.get() !=null){
            return _parentElt.get().getParentDocument();
        }
        return null;
    }
    
    public <T extends CouchbaseDocumentElement> T getFirstParentOfClass(Class<T> clazz){
        if(_parentElt!=null){
            if(_parentElt.getClass().equals(clazz)){
                return (T) (_parentElt.get());
            }
            else{
                return _parentElt.get().getFirstParentOfClass(clazz);
            }
        }
        return null;
    }
    
    public boolean validate(){
        return true;
    }
    
}
