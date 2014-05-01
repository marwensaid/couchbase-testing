package com.dreameddeath.billing.model;

import java.util.Collection;
import java.util.Collections;
//import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.dreameddeath.common.model.CouchbaseDocument;
import com.dreameddeath.common.model.CouchbaseDocumentArrayList;
import com.dreameddeath.common.model.ImmutableProperty;

public class BillingAccount extends CouchbaseDocument{
    private ImmutableProperty<String> _uid=new ImmutableProperty<String>(BillingAccount.this);
	private String _ledgerSegment;
    private String _taxProfile;
	private Type _type;
	private DateTime _creationDate;
	private Integer _billDay;
    private Integer _billingCycleLength;
	private String _currency;
	private String _paymentMethod;
    private List<BillingCycleLink> _billingCycleLinks = new CouchbaseDocumentArrayList<BillingCycleLink>(BillingAccount.this);
    
    @JsonProperty("uid")
    public String getUid() { return _uid.get(); }
    public void setUid(String uid) { _uid.set(uid); }
    
    @JsonProperty("ledgerSegment")
    public String getLedgerSegment() { return _ledgerSegment; }
    public void setLedgerSegment(String ledgerSegment) { _ledgerSegment=ledgerSegment; }
    
    @JsonProperty("taxProfile")
	public String getTaxProfile() { return _taxProfile; }
    public void setTaxProfile(String taxProfile) { _taxProfile=taxProfile; }
    
    @JsonProperty("type")
    public Type getType() { return _type; }
    public void setType(Type type) { _type=type; }
    
    @JsonProperty("creationDate")
    public DateTime getCreationDate() { return _creationDate; }
    public void setCreationDate(DateTime creationDate) { _creationDate=creationDate; }
    
    @JsonProperty("billDay")
    public Integer getBillDay() { return _billDay; }
    public void setBillDay(Integer billDay) { _billDay=billDay; }
    
    @JsonProperty("billCycleLength")
    public Integer getBillingCycleLength() { return _billingCycleLength; }
    public void setBillingCycleLength(Integer billingCycleLength) { _billingCycleLength=billingCycleLength; }
    
    @JsonProperty("currency")
    public String getCurrency() { return _currency; }
    public void setCurrency(String currency) { _currency=currency; }
    
    @JsonProperty("paymentMethod")
    public String getPaymentMethod() { return _paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { _paymentMethod=paymentMethod; }
    
    @JsonProperty("billingCycles")
    public List<BillingCycleLink> getBillingCycleLinks() { return Collections.unmodifiableList(_billingCycleLinks); }
    public BillingCycleLink getBillingCycleLink(DateTime refDate){
        for(BillingCycleLink billCycleLink:_billingCycleLinks){
            if(billCycleLink.isValidForDate(refDate)){
                return billCycleLink;
            }
        }
        return null;
    }
    public void setBillingCycleLinks(Collection<BillingCycleLink> billingCycleLinks) { _billingCycleLinks.clear();System.out.println("Adding links to ba"+billingCycleLinks); _billingCycleLinks.addAll(billingCycleLinks); }
    public void addBillingCycle(BillingCycle billingCycle){
        if(getBillingCycleLink(billingCycle.getStartDate())!=null){
            ///TODO generate a duplicate error
        }
        _billingCycleLinks.add(billingCycle.newBillingCycleLink());
        billingCycle.setBillingAccountLink(newBillingAccountLink());
    }
    
    public BillingAccountLink newBillingAccountLink(){
        return new BillingAccountLink(this);
    }
    
    /**
     * the types of billing account
     */
    public static enum Type {
        prepaid("prepaid"),
        postpaid("postpaid");
        private String _value;
        
        Type(String value){ _value = value; }
        public String toString(){ return _value; }
    }
    
    @Override
    public String toString(){
        String result=super.toString()+",\n";
        result+="uid:"+_uid+",\n";
        result+="ledgerSegment:"+_ledgerSegment+",\n";
        result+="billingCycleLinks: "+_billingCycleLinks+"\n";
        return result;
    }
}