package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.entity.User;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum SubscriptionErrorMessageBuilder implements Function<Subscription, String> { INSTANCE;
    @Override
    public String apply(Subscription subscription) {
        List<String> errorMessage = new ArrayList<>();
        LocalDateTime begin = subscription.getActivationTime();
        LocalDateTime end = subscription.getExpirationTime();
        if (end.isBefore(begin)) errorMessage.add("the activity period ends before it begins");
        if (end.isEqual(begin)) errorMessage.add("the activity period is 0 seconds");
        return StringUtils.join(errorMessage, ", ");
    }
}
