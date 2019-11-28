package com.netcracker.frolic.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name="genre")
public class Genre {
    @Id @GeneratedValue
    @Column(name="genre_id")
    private long genreId;

    @Getter
    @NaturalId
    @Column(unique=true, nullable=false, name="genre_name")
    private String genreName;

    // жанр хранит большое число ссылок на игры, но по дефолту там FetchType=LAZY, так что не должно быть проблемой
    @ManyToMany(mappedBy="genres")
    private Set<GameInfo> gameInfos = new HashSet<>();

    public Set<GameInfo> getGameInfos()
    { return this.gameInfos; }
}