package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.repository.UserRepo;
import com.netcracker.frolic.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email)
    { return repository.findByEmail(email); }

    @Transactional(readOnly = true)
    public User loadUserByUsername(String name) {
        User user = repository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(UserRepo.class.getSimpleName()
                    + " failed to find user with name " + name + " in the database.");
        }
        return repository.findByName(name);
    }

    public User save(User user) {
        validator.validate(user);
        return repository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable)
    { return repository.findAll(pageable); }


    @Override
    public boolean existsById(long id)
    { return repository.existsById(id); }
}
