package com.netcracker.frolic.validator;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * errorMessageBuilder получает и проверяет экземпляр класса, попутно строя сообщение об ошибке.
 * Должен вернуть строку с ошибкой или пустую строку в случае отсутствия ошибок.
 *
 * errorMessageHandler получает и обрабатывает сообщение об ошибке. Может, например, залогировать
 * ошибку или бросить исключение, поместив в него сообщение.
 */
public final class ValidatorImpl<T> implements Validator<T> {
    private final Function<T, String> errorMessageBuilder;
    private final Consumer<String> errorMessageHandler;

    public ValidatorImpl(Function<T, String> errorMessageBuilder, Consumer<String> errorMessageHandler)
    { this.errorMessageBuilder = errorMessageBuilder;  this.errorMessageHandler = errorMessageHandler; }

    @Override
    public void validate(T classToCheck) {
        String errorMessage = errorMessageBuilder.apply(classToCheck);
        if (!errorMessage.isEmpty()) errorMessageHandler.accept(errorMessage);
    }
}
