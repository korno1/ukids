package com.modernfamily.ukids.domain.caption.entity;

import com.modernfamily.ukids.domain.photo.entity.Photo;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_delete_caption = false")
public class Caption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="caption_id")
    private Long captionId;

    @Column(name="content", nullable = false, length = 255)
    private String content;

    @Column(name = "is_delete_caption", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    private Caption(String content, boolean isDelete, Photo photo) {
        this.content = content;
        this.isDelete = isDelete;
        this.photo = photo;
    }

    public static Caption createCaption(String content, Photo photo) {

        return new Caption(content, false, photo);
    }

    public void updateCaption(String content){
        this.content = content;
    }

    public void deleteCaption(){
        this.isDelete = true;
    }
}

