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

package com.dreameddeath.core.elasticsearch.search;

import com.dreameddeath.core.elasticsearch.ElasticSearchClient;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import rx.Observable;

/**
 * Created by Christophe Jeunesse on 26/05/2015.
 */
public class ElasticSearchSearchQueryBuilder extends SearchRequestBuilder {
    private final ElasticSearchClient client;

    public ElasticSearchSearchQueryBuilder(ElasticSearchClient client) {
        super(client.getInternalClient(), SearchAction.INSTANCE);
        this.client = client;
    }

    @Override
    public ListenableActionFuture<SearchResponse> execute() {
        throw new RuntimeException("Direct execute access forbidden");
    }

    public Observable<SearchResponse> executeAsObservable() {
        return Observable.from(super.execute()); //Todo manage errors and potentially source mapping
    }

}
