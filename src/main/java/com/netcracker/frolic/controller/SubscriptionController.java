package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.service.EmailService;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.service.SubscriptionService;
import com.netcracker.frolic.service.UserService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/subscription", produces = "application/json")
public class SubscriptionController {
    private  final SubscriptionService subscriptionService;
    private  final UserService userService;
    private  final GameInfoService gameInfoService;
    private final QueryParamResolver resolver;
    private final Validator<Subscription> validator;
    private final EmailService emailService;

    enum FindBy { USER_ID }

    SubscriptionController(QueryParamResolver resolver, SubscriptionService subscriptionService,
                           UserService userService, GameInfoService gameInfoService, EmailService emailService,
                           @Qualifier("subscriptionWebValidator") Validator<Subscription> validator) {
        this.resolver = resolver;
        this.validator = validator;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.gameInfoService = gameInfoService;
        this.emailService = emailService;
    }

    @PostMapping
    public void subscribe(@RequestBody HashMap<String, String> jsonSubscription) {
        User subscriber;
        try {
            subscriber = userService.loadUserByUsername(jsonSubscription.get("username"));
        } catch (UsernameNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No user with that name is found in the database");
        }
        int gameId;
        try {
            gameId = Integer.parseInt(jsonSubscription.get("gameId"));
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Game id is invalid");
        }
        GameInfo gameInfo = gameInfoService.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "There is no game with that ID in the database"));
        LocalDate begin = Instant.ofEpochMilli(
                Long.parseLong(jsonSubscription.get("subFirstDayDate")))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate end = Instant.ofEpochMilli(
                Long.parseLong(jsonSubscription.get("subLastDayDate")))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Subscription newSubscription = new Subscription(subscriber, gameInfo, begin, end);
        validator.validate(newSubscription);
        subscriptionService.save(newSubscription);

        Resource mailMessage = new ClassPathResource("text/MailMessage.txt");
        String stringMessage = GameInfoService.asString(mailMessage);
        stringMessage = stringMessage.replace("[game_title]", gameInfo.getTitle());
        stringMessage = stringMessage.replace("[code]", Integer.toString(gameInfo.hashCode()));
        emailService.sendMail("kotedov299@hiwave.org", "Successful subscription", stringMessage);
    }

    @GetMapping("/all")
    public Page<Map<String, String>> findSubsByUserName (
            @RequestParam(name = "user-name") String name,
            @RequestParam(name = "page-number", defaultValue = "0") int pageNumber) {

        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by("status"));
        User subscriber;
        try {
            subscriber = userService.loadUserByUsername(name);
        } catch (UsernameNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No user with that name is found in the database");
        }
        Page<Map<String, String>> subs =
                subscriptionService.findAllByUserId(subscriber.getId(), pageRequest)
                .map(SubscriptionService::subToStringMap);
        return subs;
    }

    @DeleteMapping("/{id}")
    public void cancelById(@PathVariable Long id) {
        Subscription sub = subscriptionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sub.cancel();
        subscriptionService.save(sub);
    }


    @PatchMapping
    public void  patch(@RequestBody Subscription patchedSubscription) {
        validator.validate(patchedSubscription);
        subscriptionService.save(patchedSubscription);
    }
}