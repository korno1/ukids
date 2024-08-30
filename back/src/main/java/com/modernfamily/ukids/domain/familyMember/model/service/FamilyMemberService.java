package com.modernfamily.ukids.domain.familyMember.model.service;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyDeleteDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRequestDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRoleDto;

import java.util.List;

public interface FamilyMemberService {
    void applyFamilyMember(FamilyMemberRequestDto familyMemberRequestDto);

    List<FamilyMemberDto> getFamilyMember(Long familyId);

    void approveFamilyMember(Long familyMemberId);

    void cancelFamilyMember(Long familyMemberId);

    void denyFamilyMember(Long familyMemberId);

    List<FamilyMemberDto> getApprovedFamilyMember(Long familyId);

    void setFamilyMemberRole(FamilyMemberRoleDto familyMemberRoleDto);

    void deleteFamilyMember(FamilyDeleteDto familyDeleteDto);
}
