package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.security.JsonWebTokenUtil;
import com.netcracker.frolic.service.UserService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final PasswordEncoder passwordEncoder;


    enum FindBy { EMAIL, ID, NAME }

    public UserHandlingController(UserService service, QueryParamResolver resolver, JsonWebTokenUtil tokenUtil,
                                  AuthenticationManager authenticationManager, UserService userService,
                                  PasswordEncoder passwordEncoder,
                                  @Qualifier("userWebValidator") Validator<User> validator) {
        this.service = service;
        this.resolver = resolver;
        this.validator = validator;
        this.tokenUtil = tokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public void register(@RequestBody HashMap<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String email = data.get("email");

        User newUser = new User();
        newUser.setName(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(Arrays.asList("ROLE_CLIENT"));

        validator.validate(newUser);
        service.save(newUser);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody HashMap<String, String> data) {
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
        String jsonWebToken = tokenUtil.createToken(username, userRoles);
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", jsonWebToken);
        response.put("role", userRoles.get(0).substring(5).toLowerCase());
        return response;
    }

    @GetMapping
    public Page<Map<String, String>> findUser(@RequestParam(name = "find-by") String searchParam,
                         @RequestParam(name = "query") String searchQuery) {
        FindBy findBy = resolver.resolve(FindBy.class, searchParam);
        User user;
        if (findBy == FindBy.ID) {
            try {
                user = service.findById(Long.parseLong(searchQuery))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            }
            catch (NumberFormatException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id: " + searchQuery);
            }
        } else if (findBy == FindBy.NAME) {
            user = service.loadUserByUsername(searchQuery);
        }
        else {
            user = service.findByEmail(searchQuery)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        return new PageImpl<>(Arrays.asList(UserService.userToStringMap(user)));
    }

    @GetMapping("/all")
    public Page<Map<String, String>> allUsers (
            @RequestParam(name = "page-number", defaultValue = "0") int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);
        return service.findAll(pageRequest)
                .map(UserService::userToStringMap);
    }


    @GetMapping("promote/{id}")
    public void promoteUser(@PathVariable Long id) {
        updateUserRole(id, UserService.RoleChange.PROMOTION);
    }

    @GetMapping("demote/{id}")
    public void demoteUser(@PathVariable Long id) {
        updateUserRole(id, UserService.RoleChange.DEMOTION);
    }

    private void updateUserRole(long id, UserService.RoleChange roleChange) {
        User candidate = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String role = candidate.getRoles().get(0);
        role = UserService.newRole(role, roleChange);
        candidate.setRoles(Arrays.asList(role));
        service.save(candidate);
    }
}
