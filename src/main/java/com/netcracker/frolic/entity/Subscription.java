package com.netcracker.frolic.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * подписка начинается раньше чем заканчивается
 * время её начала и окончания должны быть в будущем
 * статус подписки определяется в момент вызова геттера поля status
 * будучи один раз отменённой, подписка больше не изменит статус -- можно только создать другую
 * подписки будут храниться в HashMap в классе User, поэтому реализованы hashCode и equals.
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {
    public enum Status { ON_HOLD, ACTIVE, EXPIRED, CANCELLED;
        @JsonValue
        public String getName()
        { return String.join(" ", this.name().split("_")); }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "info_id", nullable = false)
    private GameInfo info;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(nullable = false, name = "activation_time")
    private LocalDate activationTime;

    @Column(nullable = false, name = "expiration_time")
    private LocalDate expirationTime;

    @Column(nullable = false, name = "creation_time")
    private LocalDate creationTime;

    public Subscription(User whooo, GameInfo what, LocalDate begin, LocalDate end) {
        activationTime = begin;
        expirationTime = end;
        creationTime = LocalDate.now();
        if (end.isBefore(creationTime))
            throw new IllegalArgumentException("activity period is fully in the past");
        if (begin.isBefore(creationTime))
            throw new IllegalArgumentException("activity period is partially in the past");
        this.user = whooo;
        this.info = what;
        status = (begin.isAfter(creationTime)) ? Status.ON_HOLD : Status.ACTIVE;
    }

    public Status determineStatus() {
        LocalDate now = LocalDate.now();
        status = (status == Status.CANCELLED) ? Status.CANCELLED
                : (now.isBefore(activationTime)) ? Status.ON_HOLD
                : (now.isBefore(expirationTime)) ? Status.ACTIVE
                : Status.EXPIRED;
        return status;
    }

    public void cancel()
    { this.status = Status.CANCELLED; }

    @Override public String toString()
    { return "subscription id: " + id; }
}
