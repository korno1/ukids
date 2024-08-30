package com.modernfamily.ukids.domain.schedule.dto.response;

import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.schedule.entity.Schedule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleShortInfoResponseDto {

    private Long scheduleId;

    private String title;

    private String place;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    private ScheduleShortInfoResponseDto(Long scheduleId, String title, String place, LocalDateTime startTime, LocalDateTime endTime) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ScheduleShortInfoResponseDto createResponseDto(Schedule schedule) {
        return ScheduleShortInfoResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .place(schedule.getPlace())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }

}
