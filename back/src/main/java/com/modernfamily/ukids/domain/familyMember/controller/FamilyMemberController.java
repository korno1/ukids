package com.modernfamily.ukids.domain.familyMember.controller;

import com.modernfamily.ukids.domain.familyMember.dto.FamilyDeleteDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRequestDto;
import com.modernfamily.ukids.domain.familyMember.dto.FamilyMemberRoleDto;
import com.modernfamily.ukids.domain.familyMember.message.SuccessMessage;
import com.modernfamily.ukids.domain.familyMember.model.service.FamilyMemberService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;
    private final HttpResponseUtil httpResponseUtil;

    @PostMapping
    public ResponseEntity<Map<String, Object>> applyFamilyMember(@RequestBody FamilyMemberRequestDto familyMemberRequestDto){
        familyMemberService.applyFamilyMember(familyMemberRequestDto);

        return
                httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_APPLY_FAMILY_MEMBER);

    }

    @GetMapping("/approval/{familyId}")
    public ResponseEntity<Map<String, Object>> getFamilyMember(@PathVariable("familyId") Long familyId){
        List<FamilyMemberDto> familyMembers = familyMemberService.getFamilyMember(familyId);

        return httpResponseUtil.createResponse(HttpMethodCode.GET, familyMembers);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<Map<String,Object>> getApprovedFamilyMember(@PathVariable("familyId") Long familyId){
        List<FamilyMemberDto> familyMembers = familyMemberService.getApprovedFamilyMember(familyId);
        return httpResponseUtil.createResponse(HttpMethodCode.GET, familyMembers);
    }

    @PutMapping("/{familyMemberId}")
    public ResponseEntity<Map<String, Object>> approveFamilyMember(@PathVariable("familyMemberId") Long familyMemberId){
        familyMemberService.approveFamilyMember(familyMemberId);

        return httpResponseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_APPROVE_FAMILY_MEMBER);

    }

    @PutMapping("/role")
    public ResponseEntity<Map<String, Object>> setFamilyMemberRole(@RequestBody FamilyMemberRoleDto familyMemberRoleDto){
        familyMemberService.setFamilyMemberRole(familyMemberRoleDto);

        return httpResponseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_ROLE_FAMILY_MEMBER);

    }

    @DeleteMapping("/cancellation/{familyMemberId}")
    public ResponseEntity<Map<String, Object>> cancelFamilyMember(@PathVariable("familyMemberId") Long familyMemberId){
        familyMemberService.cancelFamilyMember(familyMemberId);

        return httpResponseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_CANCEL_FAMILY_MEMBER);

    }

    @DeleteMapping("/denial/{familyMemberId}")
    public ResponseEntity<Map<String, Object>> denyFamilyMember(@PathVariable("familyMemberId") Long familyMemberId){
        familyMemberService.denyFamilyMember(familyMemberId);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_CANCEL_FAMILY_MEMBER);

    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteFamilyMember(@RequestBody FamilyDeleteDto familyDeleteDto){
        familyMemberService.deleteFamilyMember(familyDeleteDto);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_MEMBER);
    }
}
