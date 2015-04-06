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

package com.dreameddeath.core.storage;


import com.couchbase.client.java.transcoder.Transcoder;
import com.dreameddeath.core.model.document.CouchbaseDocument;

/**
 * Created by ceaj8230 on 21/11/2014.
 */
public interface ICouchbaseTranscoder<T extends CouchbaseDocument> extends Transcoder<BucketDocument<T>,T> {
    public BucketDocument<T> newDocument(T baseDocument);
    public void setKeyPrefix(String prefix);
}
