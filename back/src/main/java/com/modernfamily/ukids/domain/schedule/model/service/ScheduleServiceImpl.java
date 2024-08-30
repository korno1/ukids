package com.modernfamily.ukids.domain.schedule.model.service;

import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.mapper.FamilyMapper;
import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.modernfamily.ukids.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.modernfamily.ukids.domain.schedule.dto.response.ScheduleInfoResponseDto;
import com.modernfamily.ukids.domain.schedule.dto.response.ScheduleListResponseDto;
import com.modernfamily.ukids.domain.schedule.dto.response.ScheduleShortInfoResponseDto;
import com.modernfamily.ukids.domain.schedule.entity.Schedule;
import com.modernfamily.ukids.domain.schedule.model.repository.ScheduleRepository;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final FamilyMemberValidator familyMemberValidator;
    private final FamilyMapper familyMapper;
    private final UserMapper userMapper;

    @Transactional
    public void createSchedule(ScheduleCreateRequestDto requestDto) {
        Family family = familyMemberValidator.checkUserInFamilyMember(requestDto.getFamilyId()).getFamily();

        Schedule schedule = Schedule.createSchedule(requestDto.getTitle(), requestDto.getContent(), requestDto.getPlace(),
                requestDto.getStartTime(), requestDto.getEndTime() ,family);

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void updateSchedule(ScheduleUpdateRequestDto requestDto) {
        Family family = familyMemberValidator.checkUserInFamilyMember(requestDto.getFamilyId()).getFamily();

        Schedule existSchedule = scheduleRepository.findByScheduleId(requestDto.getScheduleId())
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_SCHEDULE_EXCEPTION));

        if(!existSchedule.getFamily().getFamilyId().equals(requestDto.getFamilyId()))
            throw new ExceptionResponse(CustomException.NOT_MATCHED_SCHEDULE_FAMILY);

        Schedule schedule = Schedule.createSchedule(requestDto.getTitle(), requestDto.getContent(), requestDto.getPlace(),
                requestDto.getStartTime(), requestDto.getEndTime() ,family);

        schedule.updateSchedule(requestDto.getScheduleId());

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule existSchedule = scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_SCHEDULE_EXCEPTION));

        familyMemberValidator.checkUserInFamilyMember(existSchedule.getFamily().getFamilyId());

        existSchedule.deleteSchedule();

        scheduleRepository.save(existSchedule);
    }

    public ScheduleInfoResponseDto getScheduleInfo(Long scheduleId) {
        Schedule existSchedule = scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_SCHEDULE_EXCEPTION));

        Family family = familyMemberValidator.checkUserInFamilyMember(existSchedule.getFamily().getFamilyId()).getFamily();

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));

        return ScheduleInfoResponseDto.createResponseDto(existSchedule, familyResponseDto);
    }

    public ScheduleListResponseDto getScheduleListByDate(Long familyId, LocalDate date) {
        Family family = familyMemberValidator.checkUserInFamilyMember(familyId).getFamily();

        List<Schedule> schedules =  scheduleRepository.findAllByFamilyIdAndDate(family, date);

        log.info("schedules : {}", schedules.size());

        List<ScheduleShortInfoResponseDto> scheduleDtoList = new ArrayList();
        for(Schedule schedule : schedules){
            scheduleDtoList.add(ScheduleShortInfoResponseDto.createResponseDto(schedule));
        }

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));

        return ScheduleListResponseDto.createResponseDto(scheduleDtoList, familyResponseDto);
    }

    public ScheduleListResponseDto getScheduleListByMonth(Long familyId, int month) {
        Family family = familyMemberValidator.checkUserInFamilyMember(familyId).getFamily();

        List<Schedule> schedules =  scheduleRepository.findAllByFamilyIdAndMonth(family, month);

        log.info("schedules : {}", schedules.size());

        List<ScheduleShortInfoResponseDto> scheduleDtoList = new ArrayList();
        for(Schedule schedule : schedules){
            scheduleDtoList.add(ScheduleShortInfoResponseDto.createResponseDto(schedule));
        }

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));

        return ScheduleListResponseDto.createResponseDto(scheduleDtoList, familyResponseDto);
    }
}
