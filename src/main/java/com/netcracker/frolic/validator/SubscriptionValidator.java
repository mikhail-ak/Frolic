package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.Subscription;

import java.time.LocalDateTime;

public class SubscriptionValidator extends Validator<Subscription> {
    @Override
    protected void check(Subscription subscription) {
        LocalDateTime begin = subscription.getActivationTime();
        LocalDateTime end = subscription.getExpirationTime();
        if (end.isBefore(begin)) error("the activity period ends before it begins");
        if (end.isEqual(begin)) error("the activity period is 0 seconds");
    }
}
