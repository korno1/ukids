package com.modernfamily.ukids.domain.growthRecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GrowthRecordResponseDto {
    private Long recordId;
    private Long writerId;
    private Long folderId;
    private String title;
    private String content;
    private LocalDate date;
    private String imageUrl;
    private String imageName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonProperty("isDelete")
    private boolean isDelete;
}
