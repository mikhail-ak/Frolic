package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    public Page<Subscription> findAllByUserId(long userId, Pageable pageable);
}
