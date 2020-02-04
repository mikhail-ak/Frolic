package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public interface UserService extends UserDetailsService {

    String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public enum RoleChange { PROMOTION, DEMOTION }

    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    User loadUserByUsername(String name);
    User save(User gameInfo);
    public Page<User> findAll(Pageable pageable);
    boolean existsById(long id);

    static Map<String, String> userToStringMap(User sub) {
        Map<String, String> subDetails = new HashMap<>();
        subDetails.put("id", Long.toString(sub.getId()));
        subDetails.put("name", sub.getUsername());
        subDetails.put("email", sub.getEmail());
        subDetails.put("role", sub.getRoles().get(0).substring(5));
        return  subDetails;
    }

    static String increaseRole(String role)
    { return (role.equals("ROLE_CLIENT")) ? "ROLE_EMPLOYEE" : role; }

    static String decreaseRole(String role) {
        return (role.equals("ROLE_ADMIN")) ? "ROLE_ADMIN"
                : (role.equals("ROLE_EMPLOYEE")) ? "ROLE_CLIENT"
                : "ROLE_NOBODY";
    }

    static String newRole(String currentRole, RoleChange roleChange) {
        return (roleChange == RoleChange.PROMOTION) ? increaseRole(currentRole)
                : decreaseRole(currentRole);
    }
}
