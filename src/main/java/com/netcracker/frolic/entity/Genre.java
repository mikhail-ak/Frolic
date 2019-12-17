package com.netcracker.frolic.entity;

import com.netcracker.frolic.cache.Identifiable;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="genre")
public class Genre implements Identifiable<Long> {
    @Id @GeneratedValue
    private long id;


    @Getter
    @Column(unique = true, nullable = false, updatable = false, name="genre_name")
    private String genreName;

    @ManyToMany(mappedBy="genres")
    private Set<GameInfo> gameInfos = new HashSet<>();

    Genre() { }

    public Genre(String genreName)
    { this.genreName = genreName; }

    public Set<GameInfo> getGameInfos()
    { return gameInfos; }

    public void addInfo(GameInfo info)
    { this.gameInfos.add(info); }

    public void removeInfo(GameInfo info)
    { this.gameInfos.remove(info); }

    @Override public Long getId()
    { return id; }

    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        Genre that = (Genre) other;
        return this.genreName.equals(that.genreName);
    }

    @Override public int hashCode()
    { return genreName.hashCode(); }

    @Override public String toString()
    { return "Genre " + genreName; }
}