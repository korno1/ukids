package com.modernfamily.ukids.domain.pictureDiary.model.service;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryPaginationDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryRequestDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryResponseDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryUpdateDto;
import com.modernfamily.ukids.domain.pictureDiary.entity.PictureDiary;
import com.modernfamily.ukids.domain.pictureDiary.mapper.PictureDiaryMapper;
import com.modernfamily.ukids.domain.pictureDiary.model.repository.CustomPictureDiaryRepository;
import com.modernfamily.ukids.domain.pictureDiary.model.repository.PictureDiaryRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.s3.S3Manager;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PictureDiaryServiceImpl implements PictureDiaryService{

    private final PictureDiaryRepository pictureDiaryRepository;
    private final CustomPictureDiaryRepository customPictureDiaryRepository;
    private final PictureDiaryMapper pictureDiaryMapper;

    private final FamilyRepository familyRepository;
    private final S3Manager s3Manager;
    private final FamilyMemberValidator familyMemberValidator;


    @Override
    public void createPictureDiary(PictureDiaryRequestDto pictureDiaryRequestDto) {
        Family family = familyRepository.findByFamilyId(pictureDiaryRequestDto.getFamilyId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        // 추후 familyId와 현재 로그인 정보를 확인해서 가족방에 속해있는지 확인
        familyMemberValidator.checkUserInFamilyMember(pictureDiaryRequestDto.getFamilyId());

        Map<String, Object> uploadParam = null;
        if(pictureDiaryRequestDto.getMultipartFile() != null){
            try{
                String path = "pictureDiary";
                uploadParam = s3Manager.uploadFile(pictureDiaryRequestDto.getMultipartFile(), path);
            }
            catch(IOException e){
                throw new RuntimeException(e);
            }

            pictureDiaryRequestDto.setImageName(uploadParam.get("originalName").toString());
            pictureDiaryRequestDto.setImageS3Name(uploadParam.get("s3FileName").toString());
            pictureDiaryRequestDto.setPictureUrl(uploadParam.get("uploadImageUrl").toString());
        }

        pictureDiaryRepository.save(pictureDiaryMapper.toPictureDiaryRequestEntity(pictureDiaryRequestDto));

    }

    @Override
    public PictureDiaryResponseDto getPictureDiary(Long pictureDiaryId) {
        PictureDiary pictureDiary = pictureDiaryRepository.findByPictureDiaryId(pictureDiaryId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PICTUREDIARY_EXCEPTION));

        return pictureDiaryMapper.toPictureDiaryResponseDto(pictureDiary);
    }

    @Override
    public PictureDiaryPaginationDto getPictureDiariesByDate(Long familyId, LocalDate date, int size, int page) {
        familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));


        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<PictureDiary> pictureDiaryPage = pictureDiaryRepository.findAllByDateAndFamily_FamilyIdAndIsDeleteFalse(date, familyId, pageable);
        if(pictureDiaryPage.isEmpty()){
            throw new ExceptionResponse(CustomException.NOT_FOUND_PICTUREDIARY_EXCEPTION);
        }
        List<PictureDiaryResponseDto> pictureDiaries = new ArrayList<>();

        for (PictureDiary pictureDiary : pictureDiaryPage) {
            pictureDiaries.add(pictureDiaryMapper.toPictureDiaryResponseDto(pictureDiary));
        }

        return new PictureDiaryPaginationDto(pictureDiaries, pictureDiaryPage.getTotalPages(), size, page);
    }

    @Override
    public PictureDiaryPaginationDto getPictureDiariesAll(Long familyId, int size, int page) {
        familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "date"));

        Page<PictureDiary> pictureDiaryPage = pictureDiaryRepository.findAllByFamily_FamilyIdAndIsDeleteFalse(familyId, pageable);

        if(pictureDiaryPage.isEmpty()){
            throw new ExceptionResponse(CustomException.NOT_FOUND_PICTUREDIARY_EXCEPTION);
        }
        List<PictureDiaryResponseDto> pictureDiaries = new ArrayList<>();

        for (PictureDiary pictureDiary : pictureDiaryPage) {
            pictureDiaries.add(pictureDiaryMapper.toPictureDiaryResponseDto(pictureDiary));
        }

        return new PictureDiaryPaginationDto(pictureDiaries, pictureDiaryPage.getTotalPages(), size, page);



    }

    @Override
    public void deletePictureDiary(Long pictureDiaryId) throws IOException {
        PictureDiary pictureDiary = pictureDiaryRepository.findByPictureDiaryId(pictureDiaryId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PICTUREDIARY_EXCEPTION));

        // 추후 가족방에 속한 사람인지 검사
        familyMemberValidator.checkUserInFamilyMember(pictureDiary.getFamily().getFamilyId());

        s3Manager.deleteFile(pictureDiary.getImageS3Name());

        customPictureDiaryRepository.deletePictureDiary(pictureDiaryId);
    }

    @Override
    public void updatePictureDiary(PictureDiaryUpdateDto pictureDiaryUpdateDto) throws IOException {
        PictureDiary pictureDiary = pictureDiaryRepository.findByPictureDiaryId(pictureDiaryUpdateDto.getPictureDiaryId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PICTUREDIARY_EXCEPTION));

        // 추후 가족방에 속한 사람인지 검사
        familyMemberValidator.checkUserInFamilyMember(pictureDiaryUpdateDto.getFamilyId());

        Map<String, Object> uploadParam = null;
        if(pictureDiaryUpdateDto.getMultipartFile() != null) {
            try {
                String path = "pictureDiary";
                uploadParam = s3Manager.uploadFile(pictureDiaryUpdateDto.getMultipartFile(), path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            s3Manager.deleteFile(pictureDiary.getImageS3Name());

            pictureDiaryUpdateDto.setImageName(uploadParam.get("originalName").toString());
            pictureDiaryUpdateDto.setImageS3Name(uploadParam.get("s3FileName").toString());
            pictureDiaryUpdateDto.setPictureUrl(uploadParam.get("uploadImageUrl").toString());
        }
        else{
            pictureDiaryUpdateDto.setImageName(pictureDiary.getImageName());
            pictureDiaryUpdateDto.setImageS3Name(pictureDiary.getImageS3Name());
            pictureDiaryUpdateDto.setPictureUrl(pictureDiary.getPictureUrl());
        }

        customPictureDiaryRepository.updatePictureDiary(pictureDiaryMapper.toPictureDiaryUpdateEntity(pictureDiaryUpdateDto));
    }
}
