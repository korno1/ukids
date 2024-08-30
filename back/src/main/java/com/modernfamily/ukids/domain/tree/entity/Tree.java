package com.modernfamily.ukids.domain.tree.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
public class Tree extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treeId;

    @Column(nullable = false)
    public long exp = 0L;   // 경험치 default = 0

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isComplete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public Tree(Family family) {
        this.family = family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

}
