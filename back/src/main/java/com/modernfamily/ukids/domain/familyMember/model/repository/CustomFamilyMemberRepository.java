package com.modernfamily.ukids.domain.familyMember.model.repository;


import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;

import java.util.List;

public interface CustomFamilyMemberRepository {
    void approveFamilyMember(Long familyMemberId);

    List<FamilyMember> getApprovedFamilyMember(Long familyId);

    void deleteFamilyMember(Long familyMemberId);
}
