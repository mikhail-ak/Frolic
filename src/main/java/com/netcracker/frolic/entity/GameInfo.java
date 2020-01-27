package com.netcracker.frolic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Информация об игре вроде логотипа, описания, рейтинга и т.п.
 * Геттер для поля id реализован вручную (без использования lombok) для соответствия его
 * реализуемому данным классом интерфейсу Identifiable.
 */
@Getter
@Setter
@Entity
@Table(name = "game_info")
public class GameInfo {
    public enum Genre { FIRST_PERSON_SHOOTER, ROLE_PLAYING, STRATEGY, QUEST, FIGHTING }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String title;

    @JsonIgnore
    @OneToOne(mappedBy = "info", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private GameFile file;

    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscriptions = new HashSet<>();

    @Embedded Rating rating;

    @Column(columnDefinition = "varchar(1023)")
    private String description;

    @OneToOne(mappedBy = "info", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private GamePic logo;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        GameInfo that = (GameInfo) other;
        return this.title.equals(that.title);
    }

    @Override public int hashCode()
    { return title.hashCode(); }

    @Override public String toString() {
        return "Information about the " + title + " game." + "Price: " + pricePerDay + ", genre: " +
            genre + ", rating: " + rating.getRating() + ", description: " + description +
                ", release date: " + releaseDate;
    }
}