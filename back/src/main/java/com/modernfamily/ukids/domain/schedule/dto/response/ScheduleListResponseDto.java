package com.modernfamily.ukids.domain.schedule.dto.response;

import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.schedule.entity.Schedule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleListResponseDto {

    private List<ScheduleShortInfoResponseDto> scheduleList;

    private FamilyResponseDto family;

    private ScheduleListResponseDto(List<ScheduleShortInfoResponseDto> scheduleList, FamilyResponseDto family) {
        this.scheduleList = scheduleList;
        this.family = family;
    }

    public static ScheduleListResponseDto createResponseDto(List<ScheduleShortInfoResponseDto> scheduleList, FamilyResponseDto familyResponseDto) {
        return new ScheduleListResponseDto(scheduleList, familyResponseDto);
    }

}
