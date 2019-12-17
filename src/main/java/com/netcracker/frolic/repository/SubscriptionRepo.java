package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Genre, Long> {
}
