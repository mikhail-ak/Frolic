package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    User save(User gameInfo);
    boolean existsById(long id);
}
