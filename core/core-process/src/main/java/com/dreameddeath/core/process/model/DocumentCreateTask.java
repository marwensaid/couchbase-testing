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

package com.dreameddeath.core.process.model;

import com.dreameddeath.core.couchbase.exception.StorageException;
import com.dreameddeath.core.dao.exception.DaoException;
import com.dreameddeath.core.dao.session.ICouchbaseSession;
import com.dreameddeath.core.model.annotation.DocumentProperty;
import com.dreameddeath.core.model.document.CouchbaseDocument;
import com.dreameddeath.core.model.property.Property;
import com.dreameddeath.core.model.property.impl.ImmutableProperty;

/**
 * Created by Christophe Jeunesse on 21/05/2014.
 */
public abstract class DocumentCreateTask<T extends CouchbaseDocument> extends AbstractTask {
    @DocumentProperty("docKey")
    private Property<String> docKey=new ImmutableProperty<String>(DocumentCreateTask.this);

    public String getDocKey(){return docKey.get(); }
    public void setDocKey(String docKey){this.docKey.set(docKey); }

    public T getDocument(ICouchbaseSession session) throws DaoException, StorageException {return (T)session.get(getDocKey());}
}
