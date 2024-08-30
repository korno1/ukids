package com.modernfamily.ukids.domain.photo.entity;

import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_delete = false")
public class Photo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "photo_original_name", nullable = false, length = 255)
    private String photoOriginalName;

    @Column(name = "photo_s3_name", nullable = false, length = 2048)
    private String photoS3Name;

    @Column(name = "photo_url", nullable = false, length = 2048)
    private String photoUrl;

    @Column(name = "is_delete", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Builder
    private Photo(String photoOriginalName, String photoS3Name, String photoUrl, boolean isDelete , Album album){
        this.photoOriginalName = photoOriginalName;
        this.photoS3Name = photoS3Name;
        this.photoUrl = photoUrl;
        this.isDelete = isDelete;
        this.album = album;
    }

    public static Photo createPhoto(Map<String, Object> uploadParam, Album album){
        return Photo.builder()
                .photoOriginalName(uploadParam.get("originalName").toString())
                .photoS3Name(uploadParam.get("s3FileName").toString())
                .photoUrl(uploadParam.get("uploadImageUrl").toString())
                .isDelete(false)
                .album(album)
                .build();
    }

    public void deletePhoto(){
        isDelete = true;
    }

}
