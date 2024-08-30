package com.modernfamily.ukids.domain.familyMember.dto;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyRole;
import lombok.Data;

@Data
public class FamilyMemberRoleDto {
    private Long userId;
    private FamilyRole familyRole;
    private Long familyId;
}
