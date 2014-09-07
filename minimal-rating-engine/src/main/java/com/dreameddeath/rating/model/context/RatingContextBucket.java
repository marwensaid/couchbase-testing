package com.dreameddeath.rating.model.context;

import com.dreameddeath.core.annotation.DocumentProperty;
import com.dreameddeath.core.model.document.CouchbaseDocumentElement;
import com.dreameddeath.core.model.property.impl.StandardProperty;
import com.dreameddeath.core.model.property.Property;

public class RatingContextBucket extends CouchbaseDocumentElement{
    @DocumentProperty("code")
    private Property<String> _code=new StandardProperty<String>(RatingContextBucket.this);
    
    public String getCode(){ return _code.get();}
    public void setCode(String code){ this._code.set(code); }
}