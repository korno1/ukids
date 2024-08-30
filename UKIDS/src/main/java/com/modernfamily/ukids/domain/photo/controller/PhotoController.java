package com.modernfamily.ukids.domain.photo.controller;

import com.modernfamily.ukids.domain.photo.dto.request.PhotoSaveRequestDto;
import com.modernfamily.ukids.domain.photo.message.SuccessMessage;
import com.modernfamily.ukids.domain.photo.model.service.PhotoService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final HttpResponseUtil httpResponseUtil;
    private final PhotoService photoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> savePhoto(@ModelAttribute PhotoSaveRequestDto requestDto) throws IOException {
        photoService.savePhoto(requestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_SAVE_PHOTO.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePhoto(@PathVariable("id") Long photoId) throws IOException {
        photoService.deletePhoto(photoId);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_PHOTO.getMessage());
    }

    @DeleteMapping("/uploaded/{photoS3Name}")
    public ResponseEntity<Map<String, Object>> deleteUploadedPhoto(@PathVariable("photoS3Name") String photoS3Name) throws IOException {
        photoService.deleteUploadedPhoto("photo/"+photoS3Name);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_PHOTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPhoto(@PathVariable("id") Long photoId) {
        return httpResponseUtil.createResponse(HttpMethodCode.GET, photoService.getPhotoInfo(photoId));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<Map<String, Object>> getPhotoList(@PathVariable("id") Long albumId,
                                                            @RequestParam(value = "size", defaultValue = "5") int size,
                                                            @RequestParam(value = "page", defaultValue = "1") int page)
    {
        return httpResponseUtil.createResponse(HttpMethodCode.GET, photoService.getPhotoList(size, page, albumId));
    }

}
