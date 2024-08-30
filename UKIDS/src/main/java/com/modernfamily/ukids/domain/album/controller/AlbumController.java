package com.modernfamily.ukids.domain.album.controller;

import com.modernfamily.ukids.domain.album.dto.request.AlbumCreateRequestDto;
import com.modernfamily.ukids.domain.album.dto.request.AlbumUpdateRequestDto;
import com.modernfamily.ukids.domain.album.message.SuccessMessage;
import com.modernfamily.ukids.domain.album.model.service.AlbumService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

    private final HttpResponseUtil httpResponseUtil;
    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAlbum(@RequestBody AlbumCreateRequestDto requestDto){
        albumService.createAlbum(requestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_ALBUM.getMessage());
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateAlbum(@RequestBody AlbumUpdateRequestDto requestDto){
        albumService.updateAlbum(requestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_ALBUM.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAlbumInfo(@PathVariable("id") Long albumId){

        return httpResponseUtil.createResponse(HttpMethodCode.GET, albumService.getAlbumInfo(albumId));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<Map<String, Object>> getAlbumInfoList(
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @PathVariable("id") Long familyId){

        return httpResponseUtil.createResponse(HttpMethodCode.GET, albumService.getAlbumInfoList(size, page, familyId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAlbum(@PathVariable("id") Long albumId){
        albumService.deleteAlbum(albumId);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_ALBUM.getMessage());
    }
}
