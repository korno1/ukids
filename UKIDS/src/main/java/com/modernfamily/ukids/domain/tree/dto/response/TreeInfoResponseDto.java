package com.modernfamily.ukids.domain.tree.dto.response;

import com.modernfamily.ukids.domain.tree.entity.Tree;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class TreeInfoResponseDto {

    private Long treeId;

    private Long exp;

    private boolean isComplete;

    private Long familyId;

    private String familyName;

    private long letterCount;

    private String createTime;

    @Builder
    private TreeInfoResponseDto(Long treeId, Long exp, boolean isComplete, Long familyId, String familyName, long letterCount, String createTime) {
        this.treeId = treeId;
        this.exp = exp;
        this.isComplete = isComplete;
        this.familyId = familyId;
        this.familyName = familyName;
        this.letterCount = letterCount;
        this.createTime = createTime;
    }

    public static TreeInfoResponseDto createResponseDto(Tree tree, long letterCount) {
        return TreeInfoResponseDto.builder()
                .treeId(tree.getTreeId())
                .exp(tree.getExp())
                .isComplete(tree.isComplete())
                .familyId(tree.getFamily().getFamilyId())
                .familyName(tree.getFamily().getName())
                .letterCount(letterCount)
                .createTime(tree.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

}
