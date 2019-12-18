package com.netcracker.frolic.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    public enum AccountStatus { BANNED, CUSTOMER, EMPLOYEE, ADMIN }

    @Id @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscriptions = new HashSet<>();
}
