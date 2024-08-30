package com.modernfamily.ukids.domain.tree.model.service;

import com.modernfamily.ukids.domain.tree.dto.request.TreeCreateRequestDto;
import com.modernfamily.ukids.domain.tree.dto.request.TreeUpdateRequestDto;
import com.modernfamily.ukids.domain.tree.dto.response.TreeInfoResponseDto;
import com.modernfamily.ukids.domain.tree.entity.Tree;

public interface TreeService {

    Tree createTree(TreeCreateRequestDto treeDto);

    TreeInfoResponseDto findByFamilyId(Long familyId);

    Tree updateTree(TreeUpdateRequestDto treeDto);
}
