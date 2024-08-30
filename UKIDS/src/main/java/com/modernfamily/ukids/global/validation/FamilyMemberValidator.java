package com.modernfamily.ukids.global.validation;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FamilyMemberValidator {

    private final FamilyMemberRepository familyMemberRepository;

    public FamilyMember checkUserInFamilyMember(Long familyId){
        String userId = CustomUserDetails.contextGetUserId();
        FamilyMember familyMember = familyMemberRepository.findByUser_IdAndFamily_FamilyId(userId, familyId).orElseThrow(() ->
                new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));

        if(!familyMember.isApproval())
            throw new ExceptionResponse(CustomException.NOT_APPROVAL_FAMILYMEMBER_EXCEPTION);

        return familyMember;
    }


}
