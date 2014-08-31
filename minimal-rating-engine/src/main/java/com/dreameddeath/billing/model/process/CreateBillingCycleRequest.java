package com.dreameddeath.billing.model.process;

import com.dreameddeath.billing.model.account.BillingAccountLink;
import com.dreameddeath.core.annotation.DocumentProperty;
import com.dreameddeath.core.model.document.CouchbaseDocumentElement;
import org.joda.time.DateTime;

/**
 * Created by ceaj8230 on 30/08/2014.
 */
public class CreateBillingCycleRequest extends CouchbaseDocumentElement {
    @DocumentProperty("ba")
    public BillingAccountLink baLink;
    @DocumentProperty("startDate")
    public DateTime startDate;

}
