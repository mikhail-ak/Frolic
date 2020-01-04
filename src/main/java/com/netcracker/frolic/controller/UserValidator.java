package com.netcracker.frolic.controller;

import com.netcracker.frolic.controller.Validator;
import com.netcracker.frolic.entity.User;

import java.util.function.Consumer;

public class UserValidator extends Validator<User> {
    @Override
    protected void validate(User user) {
        if (user.getName().length() < 4) error("user name is too short");
        if (user.getName().length() > 31) error("user name is too long");
        if (user.getPassword().length() < 8) error("user password is too short");
        if (user.getPassword().length() > 31) error("user password is too long");
    }
}
