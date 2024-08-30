package com.modernfamily.ukids.domain.family.model.repository;

import com.modernfamily.ukids.domain.family.dto.FamilyPasswordDto;

public interface CustomFamilyRepository {
    void deleteFamily(FamilyPasswordDto familyPasswordDto);
}
