package com.modernfamily.ukids.domain.game.quizResult.model.repository;

import com.modernfamily.ukids.domain.game.quizResult.entity.QuizResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    Page<QuizResult> findByParticipant_UserId(Long userId, Pageable pageable);
}
