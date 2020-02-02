package com.netcracker.frolic.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
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

    @Lob
    private byte[] installationFile;

    @Column(name="last_updated_on")
    private LocalDateTime lastUpdatedOn;

    public GameFile(byte[] installationFile)
    { setInstallationFile(installationFile); }

    @JsonValue
    String fileToBase64()
    { return "data:" + "application/zip" + ";base64," + Base64Utils.encodeToString(installationFile); }

    public void setInstallationFile(byte[] installationFile) {
        this.installationFile = installationFile;
        this.lastUpdatedOn = LocalDateTime.now();
    }

    @Override public String toString()
    { return "Installation file of the " + info.getTitle() + " game. Last updated on " + lastUpdatedOn; }
}
