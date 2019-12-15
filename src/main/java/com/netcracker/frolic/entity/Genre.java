package com.netcracker.frolic.entity;

import com.netcracker.frolic.repository.Identifiable;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="genre")
public class Genre implements Identifiable<Long> {
    @Id @GeneratedValue
    private long id;

    @NaturalId
    @Column(unique=true, nullable=false, name="genre_name")
    private String genreName;

    @ManyToMany(mappedBy="genres")
    private Set<GameInfo> gameInfos = new HashSet<>();

    public Set<GameInfo> getGameInfos() {
        return this.gameInfos;
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public void setInfo(GameInfo info) {
        this.gameInfos.add(info);
    }

    @Override
    public Long getID() {
        return id;
    }
}