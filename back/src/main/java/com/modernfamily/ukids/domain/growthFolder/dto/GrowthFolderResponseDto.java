package com.modernfamily.ukids.domain.growthFolder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GrowthFolderResponseDto {

    @Builder
    public GrowthFolderResponseDto(Long folderId, Long familyId, String folderName, LocalDateTime createTime, LocalDateTime updateTime, boolean isDelete) {
        this.folderId = folderId;
        this.familyId = familyId;
        this.folderName = folderName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDelete = isDelete;
    }

    private Long folderId;
    private Long familyId;
    private String folderName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonProperty("isDelete")
    private boolean isDelete;

}
