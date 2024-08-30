package com.modernfamily.ukids.domain.family.mapper;

import com.modernfamily.ukids.domain.family.dto.*;
import com.modernfamily.ukids.domain.family.entity.Family;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMapper {

    FamilyMapper INSTANCE = Mappers.getMapper(FamilyMapper.class);

    FamilyResponseDto toFamilyResponseDto(Family family);

    @Mapping(source = "representative", target = "user.userId")
    Family toFamilyRequestEntity(FamilyRequestDto dto);

    @Mapping(source = "representative", target = "user.userId")
    Family toFamilyUpdateEntity(FamilyUpdateDto dto);

    @Mapping(source = "user", target = "userFamilyDto")
    FamilySearchResponseDto toFamilySearchDto(Family family);

    @Mapping(source = "user.userId", target = "representative")
    @Named("familyList")
    FamilyListResponseDto toFamilyListResponseDto(Family family);

    @IterableMapping(qualifiedByName = "familyList")
    List<FamilyListResponseDto> toFamilyResponseDtoList(List<Family> families);
}
