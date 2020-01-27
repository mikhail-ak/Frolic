package com.netcracker.frolic.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * Содержит файл с игрой и дату последнего его обновления.
 * Дата обновления устанавливается автоматически при добавлении/изменении файла.
 * Соответствующие земпляры GameInfo и GameFile имеют одинаковые id.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "game_file")
public class GameFile {

    @Id
    private long info_id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private GameInfo info;

    @Column(nullable = false, name = "installation_file")
    private Blob installationFile;

    @Column(name="last_updated_on")
    private LocalDateTime lastUpdatedOn;

    public GameFile(Blob installationFile)
    { setInstallationFile(installationFile); }

    public void setInstallationFile(Blob installationFile) {
        this.installationFile = installationFile;
        this.lastUpdatedOn = LocalDateTime.now();
    }

    @Override public String toString()
    { return "Installation file of the " + info.getTitle() + " game. Last updated on " + lastUpdatedOn; }
}
