package com.modernfamily.ukids.domain.growthRecord.mapper;

import com.modernfamily.ukids.domain.growthRecord.dto.GrowthRecordRequestDto;
import com.modernfamily.ukids.domain.growthRecord.dto.GrowthRecordResponseDto;
import com.modernfamily.ukids.domain.growthRecord.dto.GrowthRecordUpdateDto;
import com.modernfamily.ukids.domain.growthRecord.entity.GrowthRecord;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrowthRecordMapper {

    GrowthRecordMapper INSTANCE = Mappers.getMapper(GrowthRecordMapper.class);

    @Mapping(source = "writerId", target = "user.userId")
    @Mapping(source = "folderId", target = "folder.folderId")
    GrowthRecord toGrowthRecordRequestEntity(GrowthRecordRequestDto growthRecordRequestDto);

    @Mapping(source = "user.userId", target = "writerId")
    @Mapping(source = "folder.folderId", target = "folderId")
    @Named("response")
    GrowthRecordResponseDto toGrowthRecordResponseDto(GrowthRecord growthRecord);

    GrowthRecord toGrowthRecordUpdateEntity(GrowthRecordUpdateDto GrowthRecordUpdateDto);

    @IterableMapping(qualifiedByName = "response")
    List<GrowthRecordResponseDto> toGrowthRecordResponseDtoList(List<GrowthRecord> growthRecord);

}
