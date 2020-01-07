package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.GameInfo;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum GameInfoErrorMessageBuilder implements Function<GameInfo, String> { INSTANCE;
    @Override
    public String apply(GameInfo info) {
        List<String> errorMessage = new ArrayList<>();

        String title = info.getTitle();
        if (title.length() < 1) errorMessage.add("title is too short");
        else if (title.length() > 255) errorMessage.add("title is too long");

        if (info.getPricePerDay().compareTo(BigDecimal.ZERO) < 0)
            errorMessage.add("negative price is not allowed");

        if (info.getDescription().length() > 1023)
            errorMessage.add("description is too long");

        if (info.getRating().getRatingsCount() <= 0)
            errorMessage.add("ratings count must be positive");
        if (info.getRating().getRatingsSum() < 0)
            errorMessage.add("ratings sum is less than zero");
        if (info.getRating().getRating() < 0)
            errorMessage.add("negative rating is not allowed");

        return StringUtils.join(errorMessage, ", ");
    }
}
