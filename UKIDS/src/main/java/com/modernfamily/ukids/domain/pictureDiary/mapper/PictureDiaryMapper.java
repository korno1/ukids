package com.modernfamily.ukids.domain.pictureDiary.mapper;

import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryRequestDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryResponseDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryUpdateDto;
import com.modernfamily.ukids.domain.pictureDiary.entity.PictureDiary;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PictureDiaryMapper {

    PictureDiaryMapper INSTANCE = Mappers.getMapper(PictureDiaryMapper.class);


    @Mapping(source = "familyId", target = "family.familyId")
    PictureDiary toPictureDiaryRequestEntity(PictureDiaryRequestDto pictureDiaryRequestDto);

    @Mapping(source = "family.familyId", target = "familyId")
    @Named("response")
    PictureDiaryResponseDto toPictureDiaryResponseDto(PictureDiary pictureDiary);

    @IterableMapping(qualifiedByName = "response")
    List<PictureDiaryResponseDto> toPictureDiaryResponseDtoList(List<PictureDiary> pictureDiaries);

    PictureDiary toPictureDiaryUpdateEntity(PictureDiaryUpdateDto pictureDiaryUpdateDto);
}
