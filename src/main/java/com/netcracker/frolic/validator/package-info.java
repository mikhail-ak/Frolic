/**
 * Валидаторы используются в контроллерах, где они проверяют полученный от пользователя экземпляр класса
 * и бросают исключение ResponseStatusException(BAD_REQUEST) в случае, если найдут ошибки.
 * Таким образом, вместо того, чтобы при каждой проверке в каждом контроллере писать, например,
 * if (!validator.validates(instanceObtainedFromUser))
 *     throw new ResponseStatusException(ResponseStatus.BAD_REQUEST, validator.getErrorMessage());
 * достаточно написать validator.validate(instanceObtainedFromUser).
 *
 * Также валидаторы используются в сервисах, где попытка сохранить невалидный экземпляр класса
 * является серьезной ошибкой в работе приложения и должна бросать другое исключение.
 *
 * Validator - функциональный интерфейс, который легко мокнуть. Таким образом, не создается жесткой
 * зависимости между валидаторами и классами, их использующими. Сами валидаторы инжектятся в виде бинов,
 * создаваемых в файлах конфигурации из пакета com.netcracker.frolic.config.
 *
 * ValidatorImpl использует стандартные функциональные интерфейсы Function и Consumer, которые реализуются
 * энумами типа [class name]ErrorMessageBuilder и [exception name]Thrower соответственно.
 * Эти энумы иммутабельны и являются синглтонами.
 * Механизм работы ValidatorImpl описан в закрепленном за ним javadoc-комменте.
 */
package com.netcracker.frolic.validator;