package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.repository.UserRepo;
import com.netcracker.frolic.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service("jpaUserService")
public class UserServiceImpl implements UserService {
    private final UserRepo repository;
    private final ValidatorImpl<User> validator;

    UserServiceImpl(UserRepo repository, @Qualifier("userJpaValidator") ValidatorImpl<User> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(long id)
    { return repository.findById(id); }

    public void updateUser(long userId, User.AccountStatus status, String password, String email) {
        repository.findById(userId).ifPresent(user -> {
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
    { return repository.findByEmail(email); }

    public User save(User user) {
        validator.validate(user);
        return repository.save(user);
    }

    @Override
    public boolean existsById(long id)
    { return repository.existsById(id); }
}
