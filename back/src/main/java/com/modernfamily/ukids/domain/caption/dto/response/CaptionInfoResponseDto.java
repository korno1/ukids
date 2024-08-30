package com.modernfamily.ukids.domain.caption.dto.response;

import com.modernfamily.ukids.domain.caption.entity.Caption;
import com.modernfamily.ukids.domain.photo.dto.response.PhotoInfoResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CaptionInfoResponseDto {

    private Long captionId;

    private String content;

    private Long photoId;

    private String photoName;

    private String photoUrl;

    public static CaptionInfoResponseDto createResponseDto(Caption caption) {
        return new CaptionInfoResponseDto(caption.getCaptionId(), caption.getContent(),
                caption.getPhoto().getPhotoId(), caption.getPhoto().getPhotoOriginalName(), caption.getPhoto().getPhotoUrl());
    }
}
