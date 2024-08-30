package com.modernfamily.ukids.domain.schedule.model.service;

import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.modernfamily.ukids.domain.schedule.dto.response.ScheduleInfoResponseDto;
import com.modernfamily.ukids.domain.schedule.dto.response.ScheduleListResponseDto;

import java.time.LocalDate;

public interface ScheduleService {
    void createSchedule(ScheduleCreateRequestDto requestDto);

    void updateSchedule(ScheduleUpdateRequestDto requestDto);

    void deleteSchedule(Long scheduleId);

    ScheduleInfoResponseDto getScheduleInfo(Long scheduleId);

    ScheduleListResponseDto getScheduleListByDate(Long familyId, LocalDate date);

    ScheduleListResponseDto getScheduleListByMonth(Long familyId, int month);

}
