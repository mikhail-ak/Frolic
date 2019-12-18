package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);
    void updateUser(long userId, User.AccountStatus status, String password, String email);
    Optional<User> findByEmail(String email);
    Optional<User> save(User gameInfo);
}
