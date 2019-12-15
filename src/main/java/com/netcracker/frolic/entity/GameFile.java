package com.netcracker.frolic.entity;

import com.netcracker.frolic.repository.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="game_file")
public class GameFile implements Identifiable<Long> {

    @Id
    private long id;

    @MapsId
    @OneToOne(fetch=FetchType.LAZY)
    private GameInfo info;

    @Column(nullable=false, name="installation_file")
    private Blob installationFile;

    @Column(name="last_updated_on")
    private LocalDateTime lastUpdatedOn;

    public GameFile(Blob installationFile) {
        setInstallationFileBlob(installationFile);
    }

    public void setInfo(GameInfo gameInfo) {
        this.info = gameInfo;
    }

    void setInstallationFileBlob(Blob file) {
        installationFile = file;
        lastUpdatedOn = LocalDateTime.now();
    }

    @Override
    public Long getID()
    { return  this.id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameFile file = (GameFile) o;
        return id == file.id &&
                Objects.equals(info, file.info) &&
                lastUpdatedOn.equals(file.lastUpdatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastUpdatedOn);
    }
}
