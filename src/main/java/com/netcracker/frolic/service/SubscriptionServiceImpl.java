package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.repository.SubscriptionRepo;
import com.netcracker.frolic.validator.SubscriptionValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
//TODO должен быть отдельный метод для проверки новосозданного экземпляра, проводящий его через
// creation time check из Validatora.
@Transactional
@Service("jpaSubscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired private SubscriptionRepo subscriptionRepo;
    @Autowired private Logger log;

    @Transactional(readOnly = true)
    public Optional<Subscription> findById(long id)
    { return subscriptionRepo.findById(id); }

    public void deleteById(long id)
    { subscriptionRepo.deleteById(id); }

    @Override
    public void cancelSubscription(long id) {
        subscriptionRepo.findById(id).ifPresent(subscription -> {
            subscription.cancel();
            log.debug("Subscription cancelled: {}", subscription);
        });
    }

    @Transactional(readOnly = true)
    public Page<Subscription> findAllByUserId(long userId)
    { return subscriptionRepo.findAllByUserId(userId); }

    public Optional<Subscription> save(Subscription sub) {
        return new SubscriptionValidator(errorMessage -> log
                .error("Tried to save invalid subscription info: {}", errorMessage))
                .getIfValid(sub)
                .map(subscription -> subscriptionRepo.save(subscription));
    }
}
