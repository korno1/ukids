package com.modernfamily.ukids.domain.schedule.model.repository;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> findAllByFamilyIdAndDate(Family family, LocalDate date);
    List<Schedule> findAllByFamilyIdAndMonth(Family family, int month);
}
