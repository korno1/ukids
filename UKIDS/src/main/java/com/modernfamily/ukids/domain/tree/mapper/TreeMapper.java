package com.modernfamily.ukids.domain.tree.mapper;

import com.modernfamily.ukids.domain.tree.dto.request.TreeCreateRequestDto;
import com.modernfamily.ukids.domain.tree.dto.response.TreeInfoResponseDto;
import com.modernfamily.ukids.domain.tree.entity.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TreeMapper {
    TreeMapper INSTANCE = Mappers.getMapper(TreeMapper.class);

    Tree toCreateEntity(TreeCreateRequestDto treeDto);

    TreeInfoResponseDto toResponseDto(Tree tree);
}
