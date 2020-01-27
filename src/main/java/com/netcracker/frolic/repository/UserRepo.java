package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByName(String name);
}
