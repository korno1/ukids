package com.modernfamily.ukids.domain.familyMember.model.repository;

import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRoleDto;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

    @Query(value = "select fm " +
            "from FamilyMember fm join fetch fm.user " +
            "where fm.family.familyId = :familyId and fm.isApproval = false")
    List<FamilyMember> getFamilyMember(Long familyId);

    Optional<FamilyMember> findByFamilyMemberId(Long familyMemberId);

    @Modifying
    @Transactional
    @Query(value = "update FamilyMember fm " +
            "set fm.isDelete = true " +
            "where fm.familyMemberId = :familyMemberId")
    void deleteFamilyMember(Long familyMemberId);

    @Modifying
    @Transactional
    @Query(value = "update FamilyMember fm " +
            "set fm.role = :#{#dto.familyRole} " +
            "where fm.user.userId = :#{#dto.userId} " +
            "and fm.family.familyId = :#{#dto.familyId}")
    void setFamilyMemberRole(@Param("dto") FamilyMemberRoleDto familyMemberRoleDto);

    Optional<FamilyMember> findByUser_IdAndFamily_FamilyId(String id, Long familyId);

    List<FamilyMember> findByUser_IdAndIsApprovalTrue(String id);
}
