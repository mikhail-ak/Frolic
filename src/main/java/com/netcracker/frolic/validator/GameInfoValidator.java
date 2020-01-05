package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.GameInfo;

import java.math.BigDecimal;

public class GameInfoValidator extends Validator<GameInfo> {
    @Override
    protected void check(GameInfo gameInfo) {
        String title = gameInfo.getTitle();
        if (title.length() < 1) error("title is too short");
        else if (title.length() > 255) error("title is too long");

        if (gameInfo.getPricePerDay().compareTo(BigDecimal.ZERO) < 0)
            error("negative price is not allowed");

        if (gameInfo.getDescription().length() > 1023)
            error("description is too long");

        if (gameInfo.getRating().getRatingsCount() <= 0)
            error("ratings count must be positive");
        if (gameInfo.getRating().getRatingsSum() < 0)
            error("ratings sum is less than zero");
        if (gameInfo.getRating().getRating() < 0)
            error("negative rating is not allowed");
    }
}
