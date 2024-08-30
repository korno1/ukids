package com.modernfamily.ukids.domain.familyMember.dto;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyRole;
import lombok.Data;

@Data
public class FamilyMemberRequestDto {
    private Long familyId;
    private Long userId;
    private FamilyRole role;
}
