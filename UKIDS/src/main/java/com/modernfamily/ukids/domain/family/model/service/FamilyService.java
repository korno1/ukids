package com.modernfamily.ukids.domain.family.model.service;

import com.modernfamily.ukids.domain.family.dto.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FamilyService {

    FamilyResponseDto getFamily(Long familyId);

    Long createFamily(FamilyRequestDto familyRequestDto) throws NoSuchAlgorithmException;

    FamilySearchResponseDto findByCode(String code);

    boolean checkPassword(FamilyPasswordDto familyPasswordDto);

    void updateFamily(FamilyUpdateDto familyUpdateDto);

    void deleteFamily(FamilyPasswordDto familyPasswordDto);

    List<FamilyListResponseDto> getFamilies();
}
