package com.modernfamily.ukids.domain.growthFolder.model.service;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderPaginationDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderRequestDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderResponseDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderUpdateRequestDto;
import com.modernfamily.ukids.domain.growthFolder.entity.GrowthFolder;
import com.modernfamily.ukids.domain.growthFolder.mapper.GrowthFolderMapper;
import com.modernfamily.ukids.domain.growthFolder.model.repository.GrowthFolderRepository;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GrowthFolderServiceImpl implements GrowthFolderService {
    private final GrowthFolderRepository growthFolderRepository;
    private final GrowthFolderMapper growthFolderMapper;
    private final FamilyMemberRepository familyMemberRepository;
    private final FamilyMemberValidator familyMemberValidator;

    @Override
    public void createGrowthFolder(GrowthFolderRequestDto growthFolderRequestDto) {

        familyMemberValidator.checkUserInFamilyMember(growthFolderRequestDto.getFamilyId());

        growthFolderRepository.save(growthFolderMapper.toGrowthFolderRequestEntity(growthFolderRequestDto));
    }

    @Override
    public GrowthFolderPaginationDto getGrowthFolders(Long familyId, int size, int page) {
        familyMemberValidator.checkUserInFamilyMember(familyId);

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<GrowthFolder> growthFolderPage = growthFolderRepository.findAllByFamily_FamilyId(familyId, pageable);
        if(!growthFolderPage.hasContent()) {
            throw new ExceptionResponse(CustomException.NOT_FOUND_GROWTHFOLDER_EXCEPTION);
        }

        // FamilyMember에 승인된 인원인지 조회
        String id = CustomUserDetails.contextGetUserId();
        FamilyMember familyMember = familyMemberRepository.findByUser_IdAndFamily_FamilyId(id, familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));

        if(!familyMember.isApproval())
            throw new ExceptionResponse(CustomException.NOT_APPROVAL_FAMILYMEMBER_EXCEPTION);

        List<GrowthFolderResponseDto> growthFolders = new ArrayList<>();

        for (GrowthFolder growthFolder : growthFolderPage) {
            growthFolders.add(growthFolderMapper.toGrowthFolderResponseDto(growthFolder));
        }
        return new GrowthFolderPaginationDto(growthFolders, growthFolderPage.getTotalPages(), size, page);

    }

    @Override
    public void updateGrowthFolder(GrowthFolderUpdateRequestDto growthFolderUpdateRequestDto) {

        // 폴더가 있는지 확인
        GrowthFolder growthFolder = growthFolderRepository.findByFolderId(growthFolderUpdateRequestDto.getFolderId())
                        .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_GROWTHFOLDER_EXCEPTION));

        // FamilyMember에 승인된 유저인지 조회
        familyMemberValidator.checkUserInFamilyMember(growthFolder.getFamily().getFamilyId());

        growthFolderRepository.updateGrowthFolder(growthFolderMapper.toGrowthFolderUpdateEntity(growthFolderUpdateRequestDto));
    }

    @Override
    public void deleteGrowthFolder(Long folderId) {
        // 폴더가 있는지 확인
        GrowthFolder growthFolder = growthFolderRepository.findByFolderId(folderId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_GROWTHFOLDER_EXCEPTION));

        // FamilyMember에 승인된 유저인지 조회
        familyMemberValidator.checkUserInFamilyMember(growthFolder.getFamily().getFamilyId());

        // 삭제 update
        growthFolderRepository.deleteGrowthFolder(folderId);
    }
}
