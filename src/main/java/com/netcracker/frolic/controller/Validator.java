package com.netcracker.frolic.controller;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Определив метод validate, можно задать логику проверки произвольных классов.
 * Внутри validate при наличии ошибок необходимо передавать краткие сообщения о них с помощью метода error.
 * После хотя бы одного вызова метода error проверяемый экзеипляр класса считается невалидным.

 * @param <T> проверяемый класс
 */
public abstract class Validator<T> {
    private String errorMessage;
    private boolean isValid = true;

    public boolean approves(T classToCheck) {
        errorMessage = "";
        this.validate(classToCheck);
        return isValid;
    }

    public String getErrorMessage()
    { return errorMessage; }

    protected void error(String error) {
        errorMessage += error + ", ";
        isValid = false;
    }

    protected abstract void validate(T classToCheck);
}
