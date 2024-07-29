package com.modernfamily.ukids.domain.familyMember.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class FamilyMember {

    public FamilyMember() {
    }

    public FamilyMember(Long familyMemberId, Family family, User user, boolean isApproval, String role, LocalDateTime approvalDate, LocalDateTime leaveDate, boolean isDelete) {
        this.familyMemberId = familyMemberId;
        this.family = family;
        this.user = user;
        this.isApproval = isApproval;
        this.role = role;
        this.approvalDate = approvalDate;
        this.leaveDate = leaveDate;
        this.isDelete = isDelete;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyMemberId;

    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isApproval;
    @Column(length = 30, nullable = false)
    private String role;

    @LastModifiedDate
    private LocalDateTime approvalDate;
    @LastModifiedDate
    private LocalDateTime leaveDate;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;


}
