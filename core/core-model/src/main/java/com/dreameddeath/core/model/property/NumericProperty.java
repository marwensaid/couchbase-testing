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

package com.dreameddeath.core.model.property;

/**
 * Created by ceaj8230 on 14/08/2014.
 */
public interface NumericProperty<T extends Number> extends Property<T> {
    public NumericProperty<T> inc(Number byVal);
    public NumericProperty<T> dec(Number byVal);
    public NumericProperty<T> mul(Number byVal);
    public NumericProperty<T> div(Number byVal);
}
