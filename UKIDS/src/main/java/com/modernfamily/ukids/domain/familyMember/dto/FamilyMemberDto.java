package com.modernfamily.ukids.domain.familyMember.dto;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyRole;
import com.modernfamily.ukids.domain.user.dto.UserFamilyDto;
import lombok.Data;

@Data
public class FamilyMemberDto {

    public FamilyMemberDto(Long familyMemberId, UserFamilyDto userFamilyDto) {
        this.familyMemberId = familyMemberId;
        this.userFamilyDto = userFamilyDto;
    }

    private Long familyMemberId;
    private FamilyRole role;
    private UserFamilyDto userFamilyDto;

}
