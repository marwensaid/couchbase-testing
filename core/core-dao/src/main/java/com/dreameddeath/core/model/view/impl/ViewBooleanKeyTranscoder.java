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

package com.dreameddeath.core.model.view.impl;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.view.ViewQuery;
import com.dreameddeath.core.model.view.IViewKeyTranscoder;

import java.util.Collection;

/**
 * Created by CEAJ8230 on 27/12/2014.
 */
public class ViewBooleanKeyTranscoder extends ViewBooleanTranscoder implements IViewKeyTranscoder<Boolean> {
    @Override public void key(ViewQuery query, Boolean value) {query.key(value);}
    @Override public void keys(ViewQuery query, Collection<Boolean> value) { query.keys(JsonArray.from(value.toArray()));}
    @Override public void startKey(ViewQuery query, Boolean value) { query.startKey(value);}
    @Override public void endKey(ViewQuery query, Boolean value) {query.endKey(value);}
}
