package com.modernfamily.ukids.domain.pictureDiary.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class PictureDiaryRequestDto {
    private Long familyId;
    private String content;
    private LocalDate date;
    private String pictureUrl;
    private String imageName;
    private String imageS3Name;
    MultipartFile multipartFile;
}
