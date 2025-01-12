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

package com.dreameddeath.installedbase.model.common;

import com.dreameddeath.core.model.annotation.DocumentProperty;
import com.dreameddeath.core.model.document.CouchbaseDocumentElement;
import com.dreameddeath.core.model.property.Property;
import com.dreameddeath.core.model.property.impl.StandardProperty;

/**
 * Created by Christophe Jeunesse on 10/08/2014.
 */
public abstract class InstalledItemRevision extends CouchbaseDocumentElement {
    /**
     *  orderId : Id of the order asking for a modification
     */
    @DocumentProperty("orderId")
    private Property<String> orderId = new StandardProperty<String>(InstalledItemRevision.this);
    /**
     *  orderItemId : the id of the order item requesting a modification
     */
    @DocumentProperty("orderItemId")
    private Property<String> orderItemId = new StandardProperty<String>(InstalledItemRevision.this);
    /**
     *  status : Status item linked to the revision
     */
    @DocumentProperty("status")
    private Property<InstalledStatus> status = new StandardProperty<InstalledStatus>(InstalledItemRevision.this);

    // orderId accessors
    public String getOrderId() { return orderId.get(); }
    public void setOrderId(String val) { orderId.set(val); }

    // orderItemId accessors
    public String getOrderItemId() { return orderItemId.get(); }
    public void setOrderItemId(String val) { orderItemId.set(val); }

    // status accessors
    public InstalledStatus getStatus() { return status.get(); }
    public void setStatus(InstalledStatus val) { status.set(val); }

}
