package com.modernfamily.ukids.domain.familyMember.model.service;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyDeleteDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRequestDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRoleDto;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.mapper.FamilyMemberMapper;
import com.modernfamily.ukids.domain.familyMember.model.repository.CustomFamilyMemberRepository;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.user.model.repository.UserRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyMemberServiceImpl implements FamilyMemberService{

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final FamilyMemberMapper familyMemberMapper;
    private final CustomFamilyMemberRepository customFamilyMemberRepository;

    @Override
    public void applyFamilyMember(FamilyMemberRequestDto familyMemberRequestDto) {
        String id = CustomUserDetails.contextGetUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        familyRepository.findByFamilyId(familyMemberRequestDto.getFamilyId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        familyMemberRequestDto.setUserId(user.getUserId());
        FamilyMember familyMember = familyMemberMapper.toFamilyMemberRequestEntity(familyMemberRequestDto);

        familyMemberRepository.save(familyMember);
    }

    @Override
    public List<FamilyMemberDto> getFamilyMember(Long familyId) {
        familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));
        List<FamilyMember> familyMembers = familyMemberRepository.getFamilyMember(familyId);

        return familyMemberMapper.toFamilyMemberDtoList(familyMembers);
    }

    @Override
    public void approveFamilyMember(Long familyMemberId) {
        customFamilyMemberRepository.approveFamilyMember(familyMemberId);
    }

    @Override
    public void cancelFamilyMember(Long familyMemberId) {
        FamilyMember familyMember = familyMemberRepository.findByFamilyMemberId(familyMemberId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));
        String id = CustomUserDetails.contextGetUserId();
        if(!familyMember.getUser().getId().equals(id)){
            throw new ExceptionResponse(CustomException.NOT_SAME_FAMILYMEMBER_USER_EXCEPTION);
        }

        if(familyMember.isApproval()){
            throw new ExceptionResponse(CustomException.APPROVAL_FAMILYMEMBER_EXCEPTION);
        }

        familyMemberRepository.deleteFamilyMember(familyMemberId);

    }

    @Override
    public void denyFamilyMember(Long familyMemberId) {
        FamilyMember familyMember = familyMemberRepository.findByFamilyMemberId(familyMemberId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));

        Family family = familyMember.getFamily();
        String id = CustomUserDetails.contextGetUserId();
        if(!family.getUser().getId().equals(id))
            throw new ExceptionResponse(CustomException.NOT_SAME_REPRESENTATIVE_EXCEPTION);

        if(familyMember.isApproval()){
            throw new ExceptionResponse(CustomException.APPROVAL_FAMILYMEMBER_EXCEPTION);
        }

        familyMemberRepository.deleteFamilyMember(familyMemberId);
    }

    @Override
    public List<FamilyMemberDto> getApprovedFamilyMember(Long familyId) {
        familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));
        List<FamilyMember> familyMembers = customFamilyMemberRepository.getApprovedFamilyMember(familyId);

        return familyMemberMapper.toFamilyMemberDtoList(familyMembers);
    }

    @Override
    public void setFamilyMemberRole(FamilyMemberRoleDto familyMemberRoleDto) {

        Family family = familyRepository.findByFamilyId(familyMemberRoleDto.getFamilyId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));
        String id = CustomUserDetails.contextGetUserId();
        if(!family.getUser().getId().equals(id))
            throw new ExceptionResponse(CustomException.NOT_SAME_REPRESENTATIVE_EXCEPTION);

        familyMemberRepository.setFamilyMemberRole(familyMemberRoleDto);

    }

    @Override
    public void deleteFamilyMember(FamilyDeleteDto familyDeleteDto) {
        int type = familyDeleteDto.getType();
        FamilyMember familyMember = familyMemberRepository.findByFamilyMemberId(familyDeleteDto.getFamilyMemberId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));
        String id = CustomUserDetails.contextGetUserId();

        // 자진 탈퇴 시 탈퇴하려는 user와 로그인 한 유저의 일치 여부
        if(type == 0) {
            if (!familyMember.getUser().getId().equals(id)) {
                throw new ExceptionResponse(CustomException.NOT_SAME_FAMILYMEMBER_USER_EXCEPTION);
            }
        }

        // 강제 탈퇴 시 family의 대표와 로그인 한 유저의 일치 여부
        else if(type == 1) {
            Family family = familyRepository.findByFamilyId(familyMember.getFamily().getFamilyId())
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));
            if (!family.getUser().getId().equals(id)) {
                throw new ExceptionResponse(CustomException.NOT_SAME_REPRESENTATIVE_EXCEPTION);
            }
        }
        else return;

        // 구성원 승인이 되지 않았다면
        if(!familyMember.isApproval()){
            throw new ExceptionResponse(CustomException.NOT_APPROVAL_FAMILYMEMBER_EXCEPTION);
        }

        customFamilyMemberRepository.deleteFamilyMember(familyDeleteDto.getFamilyMemberId());
        
    }
}
