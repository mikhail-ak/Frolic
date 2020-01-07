package com.netcracker.frolic.validator;

public interface Validator<T> {
    void validate(T classToCheck);
}
