package com.modernfamily.ukids.domain.family.model.service;

import com.modernfamily.ukids.domain.family.dto.*;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.mapper.FamilyMapper;
import com.modernfamily.ukids.domain.family.model.repository.CustomFamilyRepository;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRepresentativeDto;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyRole;
import com.modernfamily.ukids.domain.familyMember.mapper.FamilyMemberMapper;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.domain.user.dto.UserOtherDto;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.domain.user.model.repository.UserRepository;
import com.modernfamily.ukids.domain.user.model.service.UserService;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService{

    private final FamilyMapper familyMapper;
    private final FamilyRepository familyRepository;
    private final CustomFamilyRepository customFamilyRepository;
    private final UserMapper userMapper;
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberRepository familyMemberRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public FamilyResponseDto getFamily(Long familyId) {
        Family family = familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        User user = family.getUser();
        FamilyResponseDto responseDto = familyMapper.toFamilyResponseDto(family);
        responseDto.setUserFamilyDto(userMapper.toUserFamilyDto(user));

        return responseDto;
    }

    @Override
    public Long createFamily(FamilyRequestDto familyRequestDto){
        while(true) {
            String uuid = UUID.randomUUID().toString();
            byte[] uuidBytes = uuid.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes;

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                hashBytes = messageDigest.digest(uuidBytes);
            }catch (NoSuchAlgorithmException e) {
                throw new ExceptionResponse(CustomException.NOSUCH_ALGORITHM_EXCEPTION);
            }
            StringBuilder sb = new StringBuilder();
            for(int j=0; j<4; j++){
                sb.append(String.format("%02x", hashBytes[j]));
            }
            if(!familyRepository.existsByCode(sb.toString())){
               familyRequestDto.setCode(sb.toString());
               break;
            }
        }


        try{
            String id = CustomUserDetails.contextGetUserId();
            UserOtherDto userDto = userService.findByIdOther(id);
            familyRequestDto.setRepresentative(userDto.getUserId());
            //암호화
            familyRequestDto.setPassword(bCryptPasswordEncoder.encode(familyRequestDto.getPassword()));

            Family family = familyMapper.toFamilyRequestEntity(familyRequestDto);

            Family newFamilyInfo = familyRepository.save(family);

            // 가족방 생성 시 생성자(대표자) 추가
            FamilyMemberRepresentativeDto familyMemberRepresentativeDto = new FamilyMemberRepresentativeDto();
            familyMemberRepresentativeDto.setFamilyId(family.getFamilyId());
            familyMemberRepresentativeDto.setUserId(userDto.getUserId());
            familyMemberRepresentativeDto.setRole(FamilyRole.ROLE_NONE);
            FamilyMember familyMember = familyMemberMapper.toFamilyMemberRepresentativeEntity(familyMemberRepresentativeDto);
            familyMemberRepository.save(familyMember);

            return newFamilyInfo.getFamilyId();
        } catch (Exception e){
            throw new ExceptionResponse(CustomException.INPUT_FAMILY_EXCEPTION);
        }
    }

    @Override
    public FamilySearchResponseDto findByCode(String code) {
        Family family = familyRepository.findByCode(code)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));


        return familyMapper.toFamilySearchDto(family);
    }

    @Override
    public boolean checkPassword(FamilyPasswordDto familyPasswordDto) {
        Long familyId = familyPasswordDto.getFamilyId();
        Family family = familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        return bCryptPasswordEncoder.matches(familyPasswordDto.getPassword(), family.getPassword());
    }

    @Override
    public void updateFamily(FamilyUpdateDto familyUpdateDto) {
        Long familyId = familyUpdateDto.getFamilyId();

        Family family = familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        User familyRepresentative = family.getUser();
        String id = CustomUserDetails.contextGetUserId();
        User loginUser = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // 로그인한 사람과 대표자가 일치하지 않을때는 수정 불가
        if(familyRepresentative.getUserId() != loginUser.getUserId()){
            throw new ExceptionResponse(CustomException.NOT_SAME_REPRESENTATIVE_EXCEPTION);
        }

        familyUpdateDto.setPassword(bCryptPasswordEncoder.encode(familyUpdateDto.getPassword()));
        familyRepository.save(familyMapper.toFamilyUpdateEntity(familyUpdateDto));
    }

    @Override
    public void deleteFamily(FamilyPasswordDto familyPasswordDto) {
        Family family = familyRepository.findByFamilyId(familyPasswordDto.getFamilyId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));


        // 비밀번호 불일치 시 예외처리
        if(!bCryptPasswordEncoder.matches(familyPasswordDto.getPassword(), family.getPassword())){
            throw new ExceptionResponse(CustomException.NOT_SAME_PASSWORD_EXCEPTION);
        }

        User familyRepresentative = family.getUser();
        String id = CustomUserDetails.contextGetUserId();
        User loginUser = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // 로그인한 사람과 대표자가 일치하지 않을때는 삭제 불가
        if(familyRepresentative.getUserId() != loginUser.getUserId()){
            throw new ExceptionResponse(CustomException.NOT_SAME_REPRESENTATIVE_EXCEPTION);
        }
        
        // 가족 구성원 > 1 이면 삭제 불가
        customFamilyRepository.deleteFamily(familyPasswordDto);
    }

    @Override
    public List<FamilyListResponseDto> getFamilies() {
        String id = CustomUserDetails.contextGetUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        List<FamilyMember> familyMembers = familyMemberRepository.findByUser_IdAndIsApprovalTrue(id);
        List<Family> families = new ArrayList<>();
        for(FamilyMember familyMember : familyMembers){
            Family family = familyMember.getFamily();
            if(!family.isDelete())
                families.add(familyMember.getFamily());
        }


        return familyMapper.toFamilyResponseDtoList(families);
    }

}
