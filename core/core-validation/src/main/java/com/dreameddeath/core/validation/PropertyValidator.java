package com.dreameddeath.core.validation;

import com.dreameddeath.core.exception.ValidationFailedException;
import com.dreameddeath.core.exception.validation.ValidationException;
import com.dreameddeath.core.model.property.Property;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceaj8230 on 01/09/2014.
 */
public class PropertyValidator<T> implements Validator<Property<T>>{
    private Member _field;
    private List<Validator<Object>> _validationRules = new ArrayList<Validator<Object>>();

    public PropertyValidator(Member field){
        _field = field;
    }
    public void addRule(Validator<Object> validator){
        _validationRules.add(validator);
    }

    @Override
    public void validate(ValidatorContext ctxt,Property<T> elt)throws ValidationException {
        T obj = elt.get();
        List<ValidationException> eltErrors = null;
        for (Validator<Object> validator : _validationRules) {
            try {
                validator.validate(ctxt, obj);
            } catch (ValidationFailedException e) {
                if (eltErrors == null) {
                    eltErrors = new ArrayList<ValidationException>();
                }
                eltErrors.add(e);
            }
        }
        if (eltErrors != null) {
            throw new ValidationFailedException(ctxt.head(), (AccessibleObject) _field, "Errors in Property", eltErrors);
        }
    }
}