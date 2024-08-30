package com.modernfamily.ukids.domain.schedule.dto.response;

import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.schedule.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleInfoResponseDto {

    private Long scheduleId;

    private String title;

    private String content;

    private String place;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private FamilyResponseDto family;

    @Builder
    private ScheduleInfoResponseDto(Long scheduleId, String title, String content, String place, LocalDateTime startTime, LocalDateTime endTime, FamilyResponseDto family) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.family = family;
    }

    public static ScheduleInfoResponseDto createResponseDto(Schedule schedule, FamilyResponseDto familyResponseDto) {
        return ScheduleInfoResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .place(schedule.getPlace())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .family(familyResponseDto)
                .build();
    }

}
