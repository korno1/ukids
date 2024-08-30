package com.modernfamily.ukids.domain.game.quizQuestion.model.repository;

import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    Optional<QuizQuestion> findByQuizQuestionId(Long quizQuestionId);

    Page<QuizQuestion> findByWriter_UserId(Long userId, Pageable pageable);

    long countByWriter_Id(String id);

    @Query(value = "SELECT * FROM quiz_question WHERE user_id = :userId ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<QuizQuestion> findRandomQuizQuestionsByUser(@Param("count") long count, @Param("userId") Long userId);
}
