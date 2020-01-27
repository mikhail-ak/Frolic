package com.netcracker.frolic.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.sql.SQLException;

/**
 * Содержит изображение, относящееся к игре. Это могут быть логотип, обложка, концепт-арты, скриншоты и т.д.
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "picture")
public class GamePic {
    @Id
    private long picture_id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private GameInfo info;

    @Lob
    private byte[] picture;

    private String description;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    public GamePic(byte[] picture, String description, String mimeType) {
        this.picture = picture;
        this.description = description;
        this.mimeType = mimeType;
    }

    @JsonValue
    String picToBase64() throws SQLException
    { return "data:" + this.mimeType + ";base64," + Base64Utils.encodeToString(picture); }

    @Override
    public String toString() {
        return "GamePic{" +
                "picture=" + picture.toString() +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}