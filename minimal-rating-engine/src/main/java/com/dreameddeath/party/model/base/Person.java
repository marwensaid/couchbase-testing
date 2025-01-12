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

package com.dreameddeath.party.model.base;

import com.dreameddeath.core.annotation.DocumentProperty;
import com.dreameddeath.core.annotation.NotNull;
import com.dreameddeath.core.model.property.Property;
import com.dreameddeath.core.model.property.impl.StandardProperty;
import org.joda.time.DateTime;

/**
 * Created by Christophe Jeunesse on 01/08/2014.
 */
public class Person extends Party {
    @DocumentProperty("firstName") @NotNull
    private Property<String> firstName=new StandardProperty<String>(Person.this);
    @DocumentProperty("lastName") @NotNull
    private Property<String> lastName=new StandardProperty<String>(Person.this);
    @DocumentProperty("birthDate")
    private Property<DateTime> birthDate = new StandardProperty<DateTime>(Person.this);

    public String getFirstName(){return firstName.get();}
    public void setFirstName(String firstName){firstName.set(firstName);}

    public String getLastName(){return lastName.get();}
    public void setLastName(String lastName){lastName.set(lastName);}

    public DateTime getBirthDate() { return birthDate.get(); }
    public void setBirthDate(DateTime val) { birthDate.set(val); }

}
