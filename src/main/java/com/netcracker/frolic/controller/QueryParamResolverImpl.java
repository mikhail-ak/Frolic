package com.netcracker.frolic.controller;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class QueryParamResolverImpl implements QueryParamResolver {
    @Override
    public <T extends Enum<T>> T resolve(Class<T> paramEnum, String paramString) {
        if (paramString == null) return null;
        if (!paramEnum.isEnum())
            throw new IllegalArgumentException("paramEnum argument must have java.lang.Enum type, but was "
                    + paramEnum.getCanonicalName());

        String upperCaseParam = QUERY_PARAM_FORMAT.to(CaseFormat.UPPER_UNDERSCORE, paramString);
        try {
            return Enum.valueOf(paramEnum, upperCaseParam);
        } catch (IllegalArgumentException exception) {
            List<String> lowerHyphenEnumConstants = Arrays.stream(paramEnum.getEnumConstants())
                    .map(constant -> CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, constant.toString()))
                    .collect(Collectors.toList());
            String errorMessage = "Failed to resolve query parameter for " + paramEnum.getSimpleName()
                    + lowerHyphenEnumConstants + "; actual parameter was " + paramString;
            log.info(errorMessage, exception);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }
}
