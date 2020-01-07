package com.netcracker.frolic.validator;

import java.util.function.Consumer;

public enum  IllegalArgumentThrower implements Consumer<String> { INSTANCE;
    @Override
    public void accept(String errorMessage) {
        throw new IllegalArgumentException(errorMessage);
    }
}
