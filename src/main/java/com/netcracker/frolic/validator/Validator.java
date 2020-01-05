package com.netcracker.frolic.validator;

import java.util.Optional;

/**
 * Определив метод check, можно задать логику проверки произвольных классов.
 * Внутри check при наличии ошибок необходимо передавать краткие сообщения о них с помощью метода error.
 * После хотя бы одного вызова метода error проверяемый экзеипляр класса считается невалидным.

 * @param <T> проверяемый класс
 */
public abstract class Validator<T> {
    private String errorMessage;
    private boolean isValid = true;

    public Optional<T> validate(T classToCheck) {
        errorMessage = "";
        this.check(classToCheck);
        return isValid ? Optional.of(classToCheck) : Optional.empty();
    }

    public String getErrorMessage()
    { return errorMessage; }

    protected void error(String error) {
        errorMessage += error + ", ";
        isValid = false;
    }

    protected abstract void check(T classToCheck);
}
