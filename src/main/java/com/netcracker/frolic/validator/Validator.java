package com.netcracker.frolic.validator;

import java.util.function.Consumer;

/**
 * Проверить экземпляр класса на валидность, используя цепочку методов, например
 * ValidatorImpl.check(instanceToCheck)
 *              .IfValid(checkedInstance -> { do some work })
 *              .orGetError(logger::log);
 *
 * Метод isValid может реализовывать проверку произвольных классов.
 * От него требуется добавление в errorBuilder сообщений об ошибках, с помощью метода error(). Если этот метод
 * был вызван хотя бы раз, validityFlag выставляется в false. Затем значение
 * этого флага нужно вернуть из метода isValid, вызвав getValidityFlag.
 *
 * Сообщения об ошибках предполагается логировать для последующего анализа администратором. Также их можно отправлять
 * клиенту, если он взаимодействует напрямую с REST API игнорируя клиент, не разрешающий отправку невалидных данных.
 * Сами сообщения об ошибках должны быть краткими и с маленькой буквы, например "title is too short".
 * @param <T> проверяемый класс
 */
public abstract class Validator<T> {

    private static class CheckedEntity<T> {
        private final T checkedEntity;
        private final boolean isValid;
        private final String errorReasons;

        private CheckedEntity(T checkedEntity, boolean isValid, String errorReasons) {
            this.checkedEntity = checkedEntity;
            this.isValid = isValid;
            this.errorReasons = errorReasons;
        }

        public CheckedEntity<T> ifValid(Consumer<T> validEntityHandler) {
            if (this.isValid) validEntityHandler.accept(checkedEntity);
            return this;
        }

        public CheckedEntity<T> orGetErrors(Consumer<String> errorHandler) {
            if (!this.isValid) errorHandler.accept(errorReasons);
            return this;
        }
    }

    private StringBuilder errorMessageBuilder = new StringBuilder();
    private boolean validityFlag = true;

    public CheckedEntity<T> check(T checkee) {
        errorMessageBuilder.delete(errorMessageBuilder.length() -2, errorMessageBuilder.length());
        return isValid(checkee) ? new CheckedEntity<>(checkee, true, "")
                : new CheckedEntity<>(null, false, errorMessageBuilder.toString());
    }

    protected void error(String error) {
        errorMessageBuilder.append(error);
        errorMessageBuilder.append(", ");
        validityFlag = false;
    }

    protected boolean getValidityFlag()
    { return validityFlag; }

    protected abstract boolean isValid(T checkee);
}
