package com.netcracker.frolic.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
@Entity
@Table(name = "subscription")
public class Subscription {
    enum SubStatus { ACTIVE, EXPIRED, CANCELLED }

    @Id @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "info_id", nullable = false)
    private GameInfo info;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubStatus status;

    @Column(nullable = false, name = "activation_time")
    private LocalDateTime activationTime;

    @Column(nullable = false, name = "expiration_time")
    private LocalDateTime expirationTime;

    public void setActivityPeriod(LocalDateTime begin, LocalDateTime end) {
        activationTime = begin;
        expirationTime = end;
    }

    public SubStatus getStatus() {
        LocalDateTime now = LocalDateTime.now();
        status = (status == SubStatus.CANCELLED) ? SubStatus.CANCELLED
                : (now.isBefore(expirationTime)) ? SubStatus.ACTIVE
                : SubStatus.EXPIRED;
        return status;
    }

    public void cancel()
    { this.status = SubStatus.CANCELLED; }
}
