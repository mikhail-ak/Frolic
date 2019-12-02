package com.netcracker.frolic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="game_file")
public class GameFile {

    @Id
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private GameInfo info;

    @Lob
    @Column(nullable=false, name="installation_file")
    private java.sql.Blob installationFileBlob;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;

    public GameFile(java.sql.Blob installationFileBlob) {
        setInstallationFileBlob(installationFileBlob);
    }

    public void setInfo(GameInfo gameInfo) {
        this.info = gameInfo;
    }

    void setInstallationFileBlob(Blob blob) {
        installationFileBlob = blob;
        lastUpdated = LocalDateTime.now();
    }
}
