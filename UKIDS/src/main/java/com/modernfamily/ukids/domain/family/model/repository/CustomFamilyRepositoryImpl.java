package com.modernfamily.ukids.domain.family.model.repository;

import com.modernfamily.ukids.domain.family.dto.FamilyPasswordDto;
import com.modernfamily.ukids.domain.family.entity.QFamily;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomFamilyRepositoryImpl implements CustomFamilyRepository{

    private final JPAQueryFactory queryFactory;

    @Transactional
    @Override
    public void deleteFamily(FamilyPasswordDto familyPasswordDto) {
        QFamily family = QFamily.family;
        queryFactory.update(family)
                .set(family.isDelete, true)
                .where(family.familyId.eq(familyPasswordDto.getFamilyId()))
                .execute();
    }
}
