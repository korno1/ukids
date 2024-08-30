package com.modernfamily.ukids.domain.growthFolder.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "is_delete = false")
public class GrowthFolder extends BaseTimeEntity {

    public GrowthFolder() {
    }

    @Builder
    public GrowthFolder(Long folderId, Family family, String folderName, boolean isDelete) {
        this.folderId = folderId;
        this.family = family;
        this.folderName = folderName;
        this.isDelete = isDelete;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;

    @Column(length = 255, nullable = false)
    private String folderName;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;

}
