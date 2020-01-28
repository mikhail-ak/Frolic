package com.netcracker.frolic.controller;

import com.netcracker.frolic.security.JsonWebTokenUtil;
import com.netcracker.frolic.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/auth", produces = "application/json")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JsonWebTokenUtil tokenUtil;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JsonWebTokenUtil tokenUtil,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public Map<String, String> signin(@RequestBody HashMap<String, String> data) {
        try {
            String username = data.get("username");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.get("password")));
            String token = tokenUtil.createToken(username, userService.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found"))
                    .getRoles());

            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return response;
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password supplied");
        }
    }
}
