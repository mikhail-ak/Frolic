package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.security.JsonWebTokenUtil;
import com.netcracker.frolic.service.UserService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO не забыть про парсер из экселя!
@Slf4j
@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserHandlingController {

    private  final UserService service;
    private final QueryParamResolver resolver;
    private final Validator<User> validator;
    private final JsonWebTokenUtil tokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    enum FindBy { EMAIL, ID }

    public UserHandlingController(UserService service, QueryParamResolver resolver, JsonWebTokenUtil tokenUtil,
                                  AuthenticationManager authenticationManager, UserService userService,
                                  @Qualifier("userWebValidator") Validator<User> validator) {
        this.service = service;
        this.resolver = resolver;
        this.validator = validator;
        this.tokenUtil = tokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping
    public void saveUser(@RequestBody User newUser) {
        validator.validate(newUser);
        service.save(newUser);
    }

    @PostMapping("/login")
    public Map<String, String> signin(@RequestBody HashMap<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        List<String> userRoles = userService.loadUserByUsername(username).getRoles();
        try {
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password supplied");
        }
        String jsonWebToken = tokenUtil.createToken(username, userService.loadUserByUsername(username).getRoles());
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", jsonWebToken);
        response.put("role", userRoles.get(0).substring(5).toLowerCase());
        return response;
    }

    @GetMapping
    public User findUser(@RequestParam(name = "find-by") String searchParam,
                         @RequestParam(name = "query") String searchQuery) {
        FindBy findBy = resolver.resolve(FindBy.class, searchParam);
        Optional<User> user;
        if (findBy == FindBy.ID) {
            try { user = service.findById(Long.parseLong(searchQuery)); }
            catch (NumberFormatException exception)
            { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id: " + searchQuery); }
        } else { user = service.findByEmail(searchQuery); }
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public void changeUser(@RequestBody User patchedUser, @PathVariable Long id) {
        if (!service.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        validator.validate(patchedUser);
        service.save(patchedUser);
    }
}
