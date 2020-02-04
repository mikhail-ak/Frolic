package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface SubscriptionService {
    Optional<Subscription> findById(long id);
    void deleteById(long id);
    void cancelSubscription(long id);
    Page<Subscription> findAllByUserId(long userId, Pageable pageable);
    Subscription save(Subscription gameInfo);
    boolean existsById(long id);

    static Map<String, String> subToStringMap(Subscription sub) {
        Map<String, String> subDetails = new HashMap<>();
        subDetails.put("creationTime", sub.getCreationTime().toString());
        subDetails.put("activationTime", sub.getActivationTime().toString());
        subDetails.put("expirationTime", sub.getExpirationTime().toString());
        subDetails.put("status", sub.getStatus().getName());
        subDetails.put("gameTitle", sub.getInfo().getTitle());
        subDetails.put("subId", Long.toString(sub.getId()));
        return  subDetails;
    }
}
