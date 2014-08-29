package com.dreameddeath.core.annotation;

import com.dreameddeath.core.dao.validation.NotNullValidator;
import com.dreameddeath.core.dao.validation.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
}