package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.service.SubscriptionService;
import com.netcracker.frolic.validator.Validator;
import com.netcracker.frolic.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(value = "/subscription", produces = "application/json")
public class SubscriptionController {
    private  final SubscriptionService service;
    private final QueryParamResolver resolver;
    private final Validator<Subscription> validator;

    enum FindBy { USER_ID }

    SubscriptionController(QueryParamResolver resolver, SubscriptionService service,
                   @Qualifier("subscriptionWebValidator") Validator<Subscription> validator) {
        this.resolver = resolver;
        this.validator = validator;
        this.service = service;
    }

    @PostMapping
    public void subscribe(@RequestBody Subscription newSubscription) {
        validator.validate(newSubscription);
        service.save(newSubscription);
    }

    @GetMapping
    public Page<Subscription> findSubsByUserId(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                               @RequestParam(name = "find-by") String searchParam,
                                               @RequestParam(name = "id") String idString) {
        FindBy findBy = resolver.resolve(FindBy.class, searchParam);
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);
        Page<Subscription> subscription;
        try { subscription = service.findAllByUserId(Long.parseLong(idString), pageRequest); }
        catch (NumberFormatException exception)
        { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id: " + idString); }
        return subscription;
    }

    @PatchMapping
    public void  patch(@RequestBody Subscription patchedSubscription) {
        validator.validate(patchedSubscription);
        service.save(patchedSubscription);
    }
}