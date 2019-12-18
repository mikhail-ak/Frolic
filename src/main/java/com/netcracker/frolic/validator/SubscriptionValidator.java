package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.Subscription;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class SubscriptionValidator extends Validator<Subscription> {

    public boolean creationTimeCheck = false;

    public SubscriptionValidator(Consumer<String> errorMessageHandler)
    { super(errorMessageHandler); }

    @Override protected void validate(Subscription subscription) {
        LocalDateTime begin = subscription.getActivationTime();
        LocalDateTime end = subscription.getExpirationTime();
        LocalDateTime now = LocalDateTime.now();
        if (end.isBefore(begin)) error("the activity period ends before it begins");
        if (end.isEqual(begin)) error("the activity period is 0 seconds");

        if (creationTimeCheck && (begin.isBefore(now) || end.isBefore(now)))
            error("activity period is partially or fully in the past");
    }
}
