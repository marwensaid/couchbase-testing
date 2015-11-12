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

package com.dreameddeath.core.transcoder.json;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Created by Christophe Jeunesse on 29/10/2015.
 */
public class CouchbaseDocumentModule extends SimpleModule {
    private final CouchbaseDocumentIntrospector.Domain domain;
    public CouchbaseDocumentModule(CouchbaseDocumentIntrospector.Domain domain){
        this.domain = domain;
        this.setDeserializerModifier(new CouchbaseBusinessDocumentDeserializerModifier());
        /*for(EntityDef entityDef: new EntityDefinitionManager().getEntities()){
            try{
                CouchbaseDocumentStructureReflection reflection = CouchbaseDocumentStructureReflection.getClassInfo(entityDef.getClassName());
                if(reflection.getClassInfo().isInstanceOf(IVersionedEntity.class)) {
                    this.addDeserializer(reflection.getClassInfo().getCurrentClass(),new CouchbaseBusinessDocumentDeserializer())
                }
            }
        }*/
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
    }
}