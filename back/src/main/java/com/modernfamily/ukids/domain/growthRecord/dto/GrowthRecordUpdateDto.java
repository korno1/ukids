package com.modernfamily.ukids.domain.growthRecord.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class GrowthRecordUpdateDto {
    private Long recordId;
    private String title;
    private String content;
    private LocalDate date;
    private String imageUrl;
    private String imageName;
    private String imageS3Name;
    MultipartFile multipartFile;
}
