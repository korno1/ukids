package com.modernfamily.ukids.domain.family.entity;

import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Entity
@Getter
public class Family extends BaseTimeEntity {

    private static final long serialVersionUID = 1L;

    public Family() {
    }

    @Builder
    public Family(Long familyId, User user, String name, String code, boolean isDelete, String password) {
        this.familyId = familyId;
        this.user = user;
        this.name = name;
        this.code = code;
        this.isDelete = isDelete;
        this.password = password;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representative", referencedColumnName = "user_id")
    private User user;

    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 10, nullable = false, unique = true, updatable = false)
    private String code;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @Column(length = 255)
    private String password;


}
