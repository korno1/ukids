package com.modernfamily.ukids.domain.tree.controller;

import com.modernfamily.ukids.domain.tree.dto.request.TreeCreateRequestDto;
import com.modernfamily.ukids.domain.tree.dto.request.TreeUpdateRequestDto;
import com.modernfamily.ukids.domain.tree.message.SuccessMessage;
import com.modernfamily.ukids.domain.tree.model.service.TreeService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tree")
@RequiredArgsConstructor
public class TreeController {

    private final HttpResponseUtil responseUtil;
    private final TreeService treeService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTree(@RequestBody TreeCreateRequestDto treeDto) {
        treeService.createTree(treeDto);

        return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_TREE.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findByFamilyId(@PathVariable("id") Long familyId) {

        return responseUtil.createResponse(HttpMethodCode.GET, treeService.findByFamilyId(familyId));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateTreeExp(@RequestBody TreeUpdateRequestDto treeDto) {
        treeService.updateTree(treeDto);

        return responseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_TREE.getMessage());
    }

}
