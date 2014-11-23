package com.dreameddeath.party.model.base;

import com.dreameddeath.core.model.document.CouchbaseDocumentLink;

/**
 * Created by Christophe Jeunesse on 27/07/2014.
 */
public class PartyLink extends CouchbaseDocumentLink<Party> {
    public PartyLink(){}
    public PartyLink (Party party){
        super(party);
    }
    public PartyLink(PartyLink srcLink){
        super(srcLink);
    }

    @Override
    public String toString(){
        String result = "{\n"+super.toString()+",\n";
        result+="}\n";
        return result;
    }
}
