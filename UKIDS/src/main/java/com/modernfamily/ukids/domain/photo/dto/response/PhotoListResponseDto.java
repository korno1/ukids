package com.modernfamily.ukids.domain.photo.dto.response;

import com.modernfamily.ukids.domain.caption.entity.Caption;
import com.modernfamily.ukids.domain.photo.entity.Photo;
import lombok.Getter;

@Getter
public class PhotoListResponseDto {

    Long photoId;

    String fileName;

    String imgUrl;

    String s3Name;

    String caption;

    private PhotoListResponseDto(Long photoId, String fileName, String imgUrl, String s3Name, String caption) {
        this.photoId = photoId;
        this.fileName = fileName;
        this.imgUrl = imgUrl;
        this.s3Name = s3Name;
        this.caption = caption;
    }

    public static PhotoListResponseDto createResponseDto(Caption caption) {
        return new PhotoListResponseDto(caption.getPhoto().getPhotoId(), caption.getPhoto().getPhotoOriginalName(), caption.getPhoto().getPhotoUrl(), caption.getPhoto().getPhotoS3Name(), caption.getContent());
    }

}
