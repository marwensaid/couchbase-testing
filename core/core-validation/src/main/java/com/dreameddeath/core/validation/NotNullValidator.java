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

package com.dreameddeath.core.validation;

import com.dreameddeath.core.annotation.NotNull;
import com.dreameddeath.core.exception.ValidationFailedException;

import java.lang.reflect.Field;

/**
 * Created by ceaj8230 on 06/08/2014.
 */
public class NotNullValidator<T> implements Validator<T> {
    final private Field _field;
    public NotNullValidator(Field field,NotNull ann){
        _field = field;
    }

    public void validate(ValidatorContext ctxt,T value) throws ValidationFailedException {
        if(value==null){
            throw new ValidationFailedException(ctxt.head(),_field,"The field should be set");
        }
    }
}
