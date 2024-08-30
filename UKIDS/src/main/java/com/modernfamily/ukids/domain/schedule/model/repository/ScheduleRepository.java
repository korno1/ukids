package com.modernfamily.ukids.domain.schedule.model.repository;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.modernfamily.ukids.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    Optional<Schedule> findByScheduleId(Long scheduleId);

    @Override
    List<Schedule> findAllByFamilyIdAndDate(Family family, LocalDate date);
}
