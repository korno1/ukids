package com.modernfamily.ukids.domain.game.callMyNameResult.model.service;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.game.callMyNameResult.dto.CallMyNameResultSaveDto;
import com.modernfamily.ukids.domain.game.callMyNameResult.entity.CallMyNameResult;
import com.modernfamily.ukids.domain.game.callMyNameResult.model.repository.CallMyNameResultRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CallMyNameResultServiceImpl implements CallMyNameResultService {

    private final CallMyNameResultRepository resultRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Override
    public void saveResult(List<CallMyNameResultSaveDto> resultSaveDtoList) {
        List<CallMyNameResult> resultList = new ArrayList<>();

        for (CallMyNameResultSaveDto resultSaveDto : resultSaveDtoList) {
            FamilyMember familyMember = familyMemberRepository.findByUser_IdAndFamily_FamilyId(resultSaveDto.getParticipantId(), resultSaveDto.getFamilyId())
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));
            resultList.add(CallMyNameResult.createResult(resultSaveDto, familyMember.getUser(), familyMember.getFamily()));
        }

        resultRepository.saveAll(resultList);
    }

}
