package com.modernfamily.ukids.domain.pictureDiary.model.repository;

import com.modernfamily.ukids.domain.pictureDiary.entity.PictureDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PictureDiaryRepository extends JpaRepository<PictureDiary, Long> {

    Optional<PictureDiary> findByPictureDiaryId(Long pictureDiaryId);

    // 날짜별로 조회
    Page<PictureDiary> findAllByDateAndFamily_FamilyIdAndIsDeleteFalse(LocalDate date, Long familyId, Pageable pageable);

    // 날짜 상관없이 전체 조회
    Page<PictureDiary> findAllByFamily_FamilyIdAndIsDeleteFalse(Long familyId, Pageable pageable);
}
