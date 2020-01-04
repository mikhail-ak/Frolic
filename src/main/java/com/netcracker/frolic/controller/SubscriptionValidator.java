package com.netcracker.frolic.controller;

import com.netcracker.frolic.controller.Validator;
import com.netcracker.frolic.entity.Subscription;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class SubscriptionValidator extends Validator<Subscription> {

    public boolean creationTimeCheck = false;

    @Override
    protected void validate(Subscription subscription) {
        LocalDateTime begin = subscription.getActivationTime();
        LocalDateTime end = subscription.getExpirationTime();
        LocalDateTime now = LocalDateTime.now();
        if (end.isBefore(begin)) error("the activity period ends before it begins");
        if (end.isEqual(begin)) error("the activity period is 0 seconds");

        if (creationTimeCheck &&  end.isBefore(now))
            error("activity period is fully in the past");
        if (creationTimeCheck && begin.isBefore(now))
            error("activity period is partially in the past");
    }
}
