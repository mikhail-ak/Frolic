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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="game_info")
public class GameInfo {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    @Column(name="game_id")
    private long gameId;

    @Setter(AccessLevel.NONE)
    @NaturalId
    @Column(nullable=false, unique=true)
    @Size(min=1, max=255)
    private String title;

    @Setter(AccessLevel.NONE)
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
    @Column(nullable=false)
    private java.sql.Blob logoBlob;

    @Column(name="release_date")
    private LocalDate releaseDate;

    @Column(name="price_per_day", nullable=false)
    @DecimalMin("0.00")
    private BigDecimal pricePerDay;

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getGameInfos().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getGameInfos().remove(this);
    }
}