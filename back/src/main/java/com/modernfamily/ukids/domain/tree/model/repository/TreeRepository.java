package com.modernfamily.ukids.domain.tree.model.repository;

import com.modernfamily.ukids.domain.tree.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {
    Optional<Tree> findByFamily_FamilyIdAndIsCompleteFalse(Long familyId);
}
