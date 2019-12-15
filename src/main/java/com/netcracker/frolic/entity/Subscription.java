package com.netcracker.frolic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * подписка начинается раньше чем заканчивается
 * время её начала и окончания должны быть в будущем
 * статус подписки определяется в момент вызова геттера поля status
 * будучи один раз отменённой, подписка больше не изменит статус -- можно только создать другую
 * подписки будут храниться в HashMap в классе User, поэтому реализованы hashCode и equals.
 */
@Data
@Entity
public class Subscription {
    enum SubStatus { ACTIVE, EXPIRED, CANCELLED }

    @Id
    @Column(name="sub_id")
    private long subId;

    @MapsId
    @OneToOne(fetch=FetchType.LAZY)
    private User user;

    @MapsId
    @OneToOne(fetch=FetchType.LAZY)
    private GameInfo info;

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
}
