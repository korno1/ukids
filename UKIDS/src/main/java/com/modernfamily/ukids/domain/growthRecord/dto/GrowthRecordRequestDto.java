package com.modernfamily.ukids.domain.growthRecord.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class GrowthRecordRequestDto {
    private Long folderId;
    private Long writerId;
    private String title;
    private String childName;
    private String content;
    private LocalDate date;
    private String imageUrl;
    private String imageName;
    private String imageS3Name;
    MultipartFile multipartFile;
}
