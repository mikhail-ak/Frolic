package com.netcracker.frolic.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    enum AccountStatus { BANNED, CUSTOMER, EMPLOYEE, ADMIN }

    @Id @GeneratedValue
    @Column(name="user_id")
    private long userId;

    @NaturalId
    @Column(nullable=false, unique=true)
    @Size(min=3, max=21)
    private String name;

    @Column(nullable=false, unique=true)
    @Email
    private String email;

    @Column(nullable=false)
    @Size(min=8, max=31)
    private String password;

    @Column(nullable=false, name="account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="user_id")
    private Set<Subscription> subscriptions = new HashSet<>();
}
