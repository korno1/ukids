package com.modernfamily.ukids.domain.game.callMyNameResult.model.repository;

import com.modernfamily.ukids.domain.game.callMyNameResult.entity.CallMyNameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallMyNameResultRepository extends JpaRepository<CallMyNameResult, Long> {
}
