package com.modernfamily.ukids.domain.caption.model.service;

import com.modernfamily.ukids.domain.caption.dto.request.CaptionCreateRequestDto;
import com.modernfamily.ukids.domain.caption.dto.request.CaptionUpdateRequestDto;
import com.modernfamily.ukids.domain.caption.dto.response.CaptionInfoResponseDto;

public interface CaptionService {

    void createCaption(CaptionCreateRequestDto requestDto);

    void updateCaption(CaptionUpdateRequestDto requestDto);

    void deleteCaption(Long captionId);

    CaptionInfoResponseDto getCaption(Long captionId);
}
