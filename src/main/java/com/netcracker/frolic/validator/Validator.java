package com.netcracker.frolic.validator;

@FunctionalInterface
public interface Validator<T> {
    void validate(T classToCheck);
}
