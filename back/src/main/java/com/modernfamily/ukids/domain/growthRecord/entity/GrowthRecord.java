package com.modernfamily.ukids.domain.growthRecord.entity;

import com.modernfamily.ukids.domain.growthFolder.entity.GrowthFolder;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
public class GrowthRecord extends BaseTimeEntity {

    public GrowthRecord() {
    }

    @Builder
    public GrowthRecord(Long recordId, User user, GrowthFolder folder, String title, String content, LocalDate date, String imageUrl, String imageName, String imageS3Name, boolean isDelete) {
        this.recordId = recordId;
        this.user = user;
        this.folder = folder;
        this.title = title;
        this.content = content;
        this.date = date;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
        this.imageS3Name = imageS3Name;
        this.isDelete = isDelete;
    }


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="forder_id")
    private GrowthFolder folder;

    @Column(length = 255, nullable = false)
    private String title;
    @Column(length = 3000, nullable = false)
    private String content;
    private LocalDate date;
    @Column(length = 2048, nullable = true)
    private String imageUrl;
    @Column(length = 255, nullable = true)
    private String imageName;
    @Column(name="image_s3_name", length = 2048, nullable = true)
    private String imageS3Name;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;



}
