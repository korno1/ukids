package com.modernfamily.ukids.domain.familyMember.model.repository;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.entity.QFamilyMember;
import com.modernfamily.ukids.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomFamilyMemberRepositoryImpl implements CustomFamilyMemberRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    @Override
    public void approveFamilyMember(Long familyMemberId) {
        QFamilyMember familyMember = QFamilyMember.familyMember;
        jpaQueryFactory
                .update(familyMember)
                .set(familyMember.isApproval, true)
                .set(familyMember.approvalDate, LocalDateTime.now())
                .where(familyMember.familyMemberId.eq(familyMemberId))
                .execute();
    }

    @Override
    public List<FamilyMember> getApprovedFamilyMember(Long familyId) {
        QFamilyMember familyMember = QFamilyMember.familyMember;
        QUser user = QUser.user;
        return jpaQueryFactory
                .selectFrom(familyMember)
                .join(familyMember.user, user)
                .fetchJoin()
                .where(familyMember.family.familyId.eq(familyId),
                        familyMember.isApproval.eq(true))
                .fetch();
    }

    @Transactional
    @Override
    public void deleteFamilyMember(Long familyMemberId) {
        QFamilyMember familyMember = QFamilyMember.familyMember;
        jpaQueryFactory
                .update(familyMember)
                .set(familyMember.isDelete, true)
                .where(familyMember.familyMemberId.eq(familyMemberId))
                .execute();
    }


}
