package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.GameInfo;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class GameInfoValidator extends Validator<GameInfo> {

    public GameInfoValidator(Consumer<String> errorMessageHandler)
    { super(errorMessageHandler); }

    @Override protected void validate(GameInfo gameInfo) {
        String title = gameInfo.getTitle();
        if (title.length() < 1) error("title is too short");
        else if (title.length() > 255) error("title is too long");

        if (gameInfo.getPricePerDay().compareTo(BigDecimal.ZERO) < 0)
            error("price cannot be negative");

        if (gameInfo.getDescription().length() > 1023)
            error("description is too long");
    }
}
