package com.netcracker.frolic.entity;

import com.netcracker.frolic.cache.Identifiable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Информация об игре вроде логотипа, описания, рейтинга и т.п.
 * Геттер для поля id реализован вручную (без использования lombok) для соответствия его
 * реализуемому данным классом интерфейсу Identifiable.
 */
@Entity
@Getter @Setter
@Table(name = "game_info")
public class GameInfo implements Identifiable<Long> {

    @Id @GeneratedValue
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String title;

    @OneToOne(mappedBy = "info", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private GameFile file;

    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "game_genre",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscriptions = new HashSet<>();

    @Embedded Rating rating;

    private String description;

    private Blob logo;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    public void addGenre(Genre genre) {
        Objects.requireNonNull(genre, "A genre cannot be null");
        this.genres.add(genre);
        genre.getGameInfos().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getGameInfos().remove(this);
    }

    @Override public Long getId()
    { return this.id; }

    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        GameInfo that = (GameInfo) other;
        return this.title.equals(that.title);
    }

    @Override public int hashCode()
    { return title.hashCode(); }

    @Override public String toString()
    { return "Information about the " + title + " game."; }
}