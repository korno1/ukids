package com.modernfamily.ukids.domain.schedule.model.service;

import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.modernfamily.ukids.domain.schedule.dto.response.ScheduleInfoResponseDto;

public interface ScheduleService {
    void createSchedule(ScheduleCreateRequestDto requestDto);

    void updateSchedule(ScheduleUpdateRequestDto requestDto);

    void deleteSchedule(Long scheduleId);

    ScheduleInfoResponseDto getScheduleInfo(Long scheduleId);

}
