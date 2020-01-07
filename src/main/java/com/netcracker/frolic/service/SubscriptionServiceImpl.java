package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.repository.SubscriptionRepo;
import com.netcracker.frolic.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service("jpaSubscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepo repository;
    private final ValidatorImpl<Subscription> validator;

    SubscriptionServiceImpl(SubscriptionRepo repository,
                            @Qualifier("subscriptionJpaValidator") ValidatorImpl<Subscription> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public Optional<Subscription> findById(long id)
    { return repository.findById(id); }

    public void deleteById(long id)
    { repository.deleteById(id); }

    @Override
    public void cancelSubscription(long id) {
        repository.findById(id).ifPresent(subscription -> {
            subscription.cancel();
            log.debug("Subscription cancelled: {}", subscription);
        });
    }

    @Transactional(readOnly = true)
    public Page<Subscription> findAllByUserId(long userId, Pageable pageable)
    { return repository.findAllByUserId(userId, pageable); }

    public Subscription save(Subscription sub) {
        validator.validate(sub);
        return repository.save(sub);
    }

    @Override
    public boolean existsById(long id)
    { return repository.existsById(id); }
}
