package com.netcracker.frolic.controller;

import com.google.common.base.CaseFormat;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

/*
 * Несколько контроллеров требуют найти для данного параметра запроса соответствующее значение в энуме.
 * Чтобы избежать дублирования кода, выношу эту логику в класс резолвер.
 * Чтобы избежать жесткой зависимости между контроллером и резолвером, использую dependency injection.
 * Резолвер должен реализовывать данный интерфейс.
 */

/**
 * QUERY_PARAM_FORMAT -- требуемый формат параметра запроса, например
 * http://frolic.com/query?param=param-value,
 * где param-value соответствует CaseFormat.LOWER_HYPHEN.
 * Парамерты в любом другом формате, например param=CamelCaseParam не будут распознаны.
 */
@FunctionalInterface
public interface QueryParamResolver {
    CaseFormat QUERY_PARAM_FORMAT = CaseFormat.LOWER_HYPHEN;

    <T extends Enum<T>> T resolve(Class<T> paramEnum, String paramString);
}