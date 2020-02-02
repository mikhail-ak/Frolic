package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    User loadUserByUsername(String name);
    User save(User gameInfo);
    boolean existsById(long id);
}
