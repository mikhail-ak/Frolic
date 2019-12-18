package com.netcracker.frolic.validator;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Определив метод validate, можно задать логику проверки произвольных классов.
 * Внутри validate при наличии ошибок необходимо передавать краткие сообщения о них с помощью метода error.
 * После хотя бы одного вызова метода error проверяемый экзеипляр класса считается невалидным, а метод getIfValid
 * вернёт пустой Optional, при этом вызвав errorMessageHandler, задающий логику обработки сообщения об ошибке.
 *
 * Сообщения об ошибках предполагается логировать для последующего анализа администратором. Также их можно отправлять
 * клиенту, если он взаимодействует напрямую с REST API и вводит невалидные данные.
 * Сами сообщения об ошибках должны быть краткими и с маленькой буквы, например "title is too short".
 * Это удобно для формирования сообщения о нескольких ошибках с их перечислением.
 * @param <T> проверяемый класс
 */
public abstract class Validator<T> {
    private StringBuilder errorMessageBuilder = new StringBuilder();
    private Consumer<String> errorMessageHandler;
    private boolean isValid = true;

    protected Validator(Consumer<String> errorMessageHandler)
    { this.errorMessageHandler = errorMessageHandler; }

    public Optional<T> getIfValid(T classToCheck) {
        this.validate(classToCheck);
        if (!this.isValid) errorMessageHandler.accept(errorMessageBuilder.toString());
        return this.isValid ? Optional.of(classToCheck)
                : Optional.empty();
    }

    protected void error(String error) {
        errorMessageBuilder.append(error);
        errorMessageBuilder.append(", ");
        isValid = false;
    }

    protected abstract void validate(T classToCheck);
}
