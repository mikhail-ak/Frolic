package com.netcracker.frolic.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * подписка начинается раньше чем заканчивается
 * время её начала и окончания должны быть в будущем
 * статус подписки определяется в момент вызова геттера поля status
 * будучи один раз отменённой, подписка больше не изменит статус -- можно только создать другую
 * подписки будут храниться в HashMap в классе User, поэтому реализованы hashCode и equals.
 */
@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name="subscription")
public class Subscription {
    enum SubStatus { ACTIVE, EXPIRED, CANCELLED }

    @Id @GeneratedValue
    @Column(name="sub_id")
    private long subId;

    @Column(name="user_id", nullable=false)
    private long userId;

    @Column(name="game_id", nullable=false)
    private long gameId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private SubStatus status;

    @Column(nullable=false, name="activation_time")
    private LocalDateTime activationTime;

    @Column(nullable=false, name="expiration_time")
    private LocalDateTime expirationTime;

    public void setActivityPeriod(LocalDateTime begin, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();
        if (begin.isBefore(now) || end.isBefore(now))
            throw new IllegalArgumentException("Activity period must be in the future");
        if (end.isBefore(begin) || end.isEqual(begin))
            throw new IllegalArgumentException("Illegal activity period");

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return subId == that.subId &&
                userId == that.userId &&
                gameId == that.gameId &&
                status == that.status &&
                activationTime.equals(that.activationTime) &&
                expirationTime.equals(that.expirationTime);
    }

    @Override
    public int hashCode()
    { return Objects.hash(subId, userId, gameId, status, activationTime, expirationTime); }
}
