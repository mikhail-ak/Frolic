package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.repository.UserRepo;
import com.netcracker.frolic.controller.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service("jpaUserService")
public class UserServiceImpl implements UserService {
    @Autowired UserRepo userRepo;
    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional(readOnly = true)
    public Optional<User> findById(long id)
    { return userRepo.findById(id); }

    public void updateUser(long userId, User.AccountStatus status, String password, String email) {
        userRepo.findById(userId).ifPresent(user -> {
            if (user.getAccountStatus() == User.AccountStatus.ADMIN) {
                log.warn("An attempt to change admin's account has been made");
            } else {
                user.setAccountStatus(status);
                user.setPassword(password);
                user.setEmail(email);
            }
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email)
    { return userRepo.findByEmail(email); }

    public User save(User user) {
        return userRepo.save(user);
    }
}
