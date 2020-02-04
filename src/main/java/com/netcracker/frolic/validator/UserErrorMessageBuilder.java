package com.netcracker.frolic.validator;

import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.service.UserService;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum  UserErrorMessageBuilder implements Function<User, String> { INSTANCE;
    @Override
    public String apply(User user) {
        List<String> errorMessage = new ArrayList<>();
        if (user.getName().length() < 4) errorMessage.add("user name is too short");
        if (user.getName().length() > 31) errorMessage.add("user name is too long");
        if (user.getPassword().length() < 8) errorMessage.add("user password is too short");
        if (!user.getEmail().matches(UserService.EMAIL_REGEXP))
            errorMessage.add("email is invalid");

        return StringUtils.join(errorMessage, ", ");
    }
}
