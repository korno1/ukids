package com.modernfamily.ukids.domain.game.callMyNameResult.model.service;

import com.modernfamily.ukids.domain.game.callMyNameResult.dto.CallMyNameResultSaveDto;

import java.util.List;

public interface CallMyNameResultService {

    void saveResult(List<CallMyNameResultSaveDto> resultSaveDtoList);
}
