package com.dreameddeath.common.model;

import com.fasterxml.jackson.annotation.JsonValue;

public interface Property<T>  {
    @JsonValue
    public  T get();
    public boolean set(T value);
}
