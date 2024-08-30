package com.modernfamily.ukids.domain.letter.model.repository;

import com.modernfamily.ukids.domain.tree.entity.Tree;


public interface LetterRepositoryCustom {
    long updateLettersOpenStatusByTree(Tree tree);

    void updateLetterRead(Long letterId);
}
