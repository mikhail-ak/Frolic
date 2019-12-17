package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.Subscription;

import java.time.LocalDateTime;

public class SubscriptionValidator extends Validator<Subscription> {

    public boolean creationTimeCheck = false;

    @Override protected boolean isValid(Subscription subscription) {
        LocalDateTime begin = subscription.getActivationTime();
        LocalDateTime end = subscription.getExpirationTime();
        if (end.isBefore(begin)) error("the activity period ends before it begins");
        if (end.isEqual(begin)) error("the activity period is 0 seconds");

        if (creationTimeCheck) creationTimeCheck = creationTimeCheck(subscription);
        return creationTimeCheck ?
                getValidityFlag() && creationTimeCheck(subscription)
                : getValidityFlag();
    }

    /**
     * Дополнительная проверка для только что созданных подписок
     */
    private boolean creationTimeCheck(Subscription subscription) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = subscription.getActivationTime();
        LocalDateTime end = subscription.getExpirationTime();
        if (begin.isBefore(now) || end.isBefore(now))
            error("activity period is partially or fully in the past");
        return isValid(subscription);
    }

    public void performCreationTimeCheck()
    { creationTimeCheck = true; }
}
