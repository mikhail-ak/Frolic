package com.netcracker.frolic.entity;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="users")
public class User {
    enum AccountStatus { BANNED, CUSTOMER, EMPLOYEE, ADMIN }

    @Id @GeneratedValue
    private long id;

    @NaturalId
    @Size(min=3, max=21)
    @Column(nullable=false, unique=true)
    private String name;

    @Email
    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    @Size(min=8, max=31)
    private String password;

    @Column(nullable=false, name="account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="id")
    private Set<Subscription> subscriptions = new HashSet<>();
}
