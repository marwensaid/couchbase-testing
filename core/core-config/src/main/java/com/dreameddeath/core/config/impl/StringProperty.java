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

package com.dreameddeath.core.config.impl;

import com.dreameddeath.core.config.AbstractProperty;
import com.dreameddeath.core.config.PropertyChangedCallback;

/**
 * Created by CEAJ8230 on 04/02/2015.
 */
public class StringProperty extends AbstractProperty<String> {

    public StringProperty(String name, String defaultValue) {
        super(new StringExtendedProperty(name,defaultValue));
    }
    public StringProperty(String name, String defaultValue,PropertyChangedCallback<String> callback) {
        super(new StringExtendedProperty(name,defaultValue),callback);
    }

    public String get(){return getValue();}

    protected static class StringExtendedProperty extends AbstractProperty.ExtendedPropertyWrapper<String> {
        public StringExtendedProperty(String name,String defaultValue){
            super(name,defaultValue);
        }
        @Override
        public String getValue() {
            return prop.getString(defaultValue);
        }
    }}
