package com.netcracker.frolic.entity;

import com.netcracker.frolic.repository.Identifiable;
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

@Getter
@Setter
@Entity
@Table(name="game_info")
public class GameInfo implements Identifiable<Long> {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NaturalId
    @Column(nullable=false, unique=true)
    @Size(min=1, max=255)
    private String title;

    @OneToOne(mappedBy="info", cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional=false)
    private GameFile file;

    @DecimalMin("0.00")
    @Column(name="price_per_day", nullable=false)
    private BigDecimal pricePerDay;

    @ManyToMany(cascade={ CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="game_genre",
            joinColumns = @JoinColumn(name="game_id"),
            inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @Embedded
    Rating rating;

    @Max(1023)
    private String description;

    private Blob logo;

    @Column(name="release_date")
    private LocalDate releaseDate;

    public GameInfo(String title, GameFile file, BigDecimal pricePerDay, Rating rating) {
        this.title = title;
        this.file = file;
        this.pricePerDay = pricePerDay;
        this.rating = rating;
    }

    public void addGenre(Genre genre) {
        Objects.requireNonNull(genre, "A genre cannot be null");
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
        return id == gameInfo.id &&
                title.equals(gameInfo.title) &&
                pricePerDay.equals(gameInfo.pricePerDay) &&
                Objects.equals(genres, gameInfo.genres) &&
                Objects.equals(rating, gameInfo.rating) &&
                Objects.equals(description, gameInfo.description) &&
                Objects.equals(releaseDate, gameInfo.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id);
    }

    @Override
    public Long getID()
    { return this.id; }
}