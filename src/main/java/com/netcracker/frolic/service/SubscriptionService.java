package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubscriptionService {
    Optional<Subscription> findById(long id);
    void deleteById(long id);
    void cancelSubscription(long id);
    Page<Subscription> findAllByUserId(long userId, Pageable pageable);
    Subscription save(Subscription gameInfo);
}
