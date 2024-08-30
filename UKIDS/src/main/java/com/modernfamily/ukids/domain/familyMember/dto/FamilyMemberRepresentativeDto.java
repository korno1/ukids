package com.modernfamily.ukids.domain.familyMember.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FamilyMemberRepresentativeDto {
    private Long familyId;
    private Long userId;
    private FamilyRole role;
    private boolean isApproval = true;
    private LocalDateTime approvalDate = LocalDateTime.now();
}
