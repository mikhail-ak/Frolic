package com.netcracker.frolic.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="game_info")
public class GameInfo {

    @Setter(AccessLevel.PRIVATE)
    @Id @GeneratedValue
    @Column(name="game_id")
    private long gameId;

    @NaturalId
    @Column(nullable = false, unique = true)
    @Size(min=1, max=255)
    private String title;

    @OneToOne(mappedBy = "info", cascade = CascadeType.ALL,
        fetch = FetchType.LAZY, optional = false)
    private GameFile file;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "game_genre",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @Embedded
    Rating rating;

    @Max(1023)
    private String description;

    @Lob
    @Column(name = "logo")
    private Blob logoBlob;

    @Column(name="release_date")
    private LocalDate releaseDate;

    @Column(name="price_per_day", nullable = false)
    @DecimalMin("0.00")
    private BigDecimal pricePerDay;

    public GameInfo(String title, BigDecimal pricePerDay) {
        this.title = title;
        this.pricePerDay = pricePerDay;
    }

    GameInfo() {}

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getGameInfos().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getGameInfos().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInfo gameInfo = (GameInfo) o;
        return gameId == gameInfo.gameId &&
                title.equals(gameInfo.title) &&
                Objects.equals(genres, gameInfo.genres) &&
                Objects.equals(description, gameInfo.description) &&
                Objects.equals(releaseDate, gameInfo.releaseDate) &&
                pricePerDay.equals(gameInfo.pricePerDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, title, genres, description, releaseDate, pricePerDay);
    }
}