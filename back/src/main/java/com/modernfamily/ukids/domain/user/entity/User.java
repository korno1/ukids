package com.modernfamily.ukids.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Getter
public class User extends BaseTimeEntity {

    public User() {
    }

    @Builder
    public User(Long userId, String id, String password, String name, String birthDate, String email, String phone, String imageName, String profileImage, Role role, boolean isDelete) {
        this.userId = userId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.imageName = imageName;
        this.profileImage = profileImage;
        this.role = role;
        this.isDelete = isDelete;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 45, nullable = false, unique = true)
    private String id;
    @Column(length = 255, nullable = false)
    private String password;
    @Column(length = 45, nullable = false)
    private String name;
    private String birthDate;
    @Column(length = 45, unique = true)
    private String email;
    @Column(length = 45, unique = true)
    private String phone;
    @Column(length = 255)
    private String imageName;
    @Column(length = 255)
    private String profileImage;

//    @ColumnDefault("ROLE_USER")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;


}
