package com.modernfamily.ukids.domain.letter.model.repository;

import com.modernfamily.ukids.domain.letter.entity.QLetter;
import com.modernfamily.ukids.domain.tree.entity.Tree;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LetterRepositoryImpl implements LetterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QLetter letter = QLetter.letter;

    @Modifying
    @Transactional
    public long updateLettersOpenStatusByTree(Tree tree) {
        return queryFactory
                .update(letter)
                .set(letter.isOpen, true)
                .where(letter.tree.eq(tree)
                        .and(letter.isOpen.eq(false)))
                .execute();
    }

    @Modifying
    @Transactional
    @Override
    public void updateLetterRead(Long letterId) {
        queryFactory
                .update(letter)
                .set(letter.isRead, true)
                .where(letter.letterId.eq(letterId))
                .execute();
    }
}
