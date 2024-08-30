package com.modernfamily.ukids.domain.photo.model.service;

import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.album.model.repository.AlbumRepository;
import com.modernfamily.ukids.domain.caption.entity.Caption;
import com.modernfamily.ukids.domain.caption.model.repository.CaptionRepository;
import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.mapper.FamilyMapper;
import com.modernfamily.ukids.domain.photo.dto.request.PhotoSaveRequestDto;
import com.modernfamily.ukids.domain.photo.dto.response.PhotoInfoResponseDto;
import com.modernfamily.ukids.domain.photo.dto.response.PhotoListPagenationResponseDto;
import com.modernfamily.ukids.domain.photo.dto.response.PhotoListResponseDto;
import com.modernfamily.ukids.domain.photo.entity.Photo;
import com.modernfamily.ukids.domain.photo.model.repository.PhotoRepository;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.s3.S3Manager;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final CaptionRepository captionRepository;
    private final AlbumRepository albumRepository;
    private final S3Manager s3Manager;
    private final FamilyMemberValidator familyMemberValidator;
    private final FamilyMapper familyMapper;
    private final UserMapper userMapper;

    @Transactional
    public void savePhoto(PhotoSaveRequestDto requestDto) throws IOException {

        Family family = familyMemberValidator.checkUserInFamilyMember(requestDto.getFamilyId()).getFamily();

        Album album = albumRepository.findByDateAndFamily_FamilyId(requestDto.getDate(), requestDto.getFamilyId())
                .orElseGet(() -> {
                    return albumRepository.save(
                            Album.createAlbum(
                                    requestDto.getDate(),
                                    requestDto.getDate().toString(),
                                    family
                            )
                    );
                });

        Map<String, Object> uploadParam = s3Manager.uploadFile(requestDto.getMultipartFile(), "photo");

        Photo photo = Photo.createPhoto(uploadParam, album);

        photoRepository.save(photo);

        String caption = requestDto.getCaption() == null? "" : requestDto.getCaption();
        captionRepository.save(Caption.createCaption(caption, photo));
    }

    @Transactional
    public void deletePhoto(Long photoId) throws IOException {
        Photo photo = photoRepository.findByPhotoId(photoId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_PHOTO_EXCEPTION));

        familyMemberValidator.checkUserInFamilyMember(photo.getAlbum().getFamily().getFamilyId());

        s3Manager.deleteFile(photo.getPhotoS3Name());

        captionRepository.deleteAllByPhoto(photo);
        photo.deletePhoto();
        photoRepository.save(photo);
    }

    public PhotoInfoResponseDto getPhotoInfo(Long photoId) {
        Photo photo = photoRepository.findByPhotoId(photoId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_PHOTO_EXCEPTION));

        Family family = familyMemberValidator.checkUserInFamilyMember(photo.getAlbum().getFamily().getFamilyId()).getFamily();

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));

        String caption = captionRepository.findByPhoto_PhotoId(photoId)
                .map(Caption::getContent)
                .orElse("");

        return PhotoInfoResponseDto.createResponseDto(photo, familyResponseDto, caption);
    }

    public PhotoListPagenationResponseDto getPhotoList(int size, int page, Long albumId) {
        Album album = albumRepository.findByAlbumId(albumId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_ALBUM_EXCEPTION));

        Family family = familyMemberValidator.checkUserInFamilyMember(album.getFamily().getFamilyId()).getFamily();

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));

        Pageable pageable = PageRequest.of(--page, size);
//        Page<Photo> photoPage = photoRepository.findAllByAlbum_AlbumIdOrderByCreateTimeDesc(albumId, pageable);
        Page<Caption> photoPage = captionRepository.findAllByPhoto_Album_AlbumIdOrderByCreateTimeDesc(albumId, pageable);

        List<Caption> photoList = photoPage.getContent();
        List<PhotoListResponseDto> responseDtoList = new ArrayList<>();
        log.info("Album List size : {}", photoList.size());
        for (Caption caption : photoList) {
            responseDtoList.add(PhotoListResponseDto.createResponseDto(caption));
        }

        PhotoListPagenationResponseDto pagenationResponseDto =
                PhotoListPagenationResponseDto.createResponseDto
                        (photoPage.getNumberOfElements(), photoPage.getNumber()+1, photoPage.getTotalPages(), album, responseDtoList, familyResponseDto);

        return pagenationResponseDto;
    }

    @Transactional
    @Override
    public void deleteUploadedPhoto(String photoS3Name) throws IOException {

        s3Manager.deleteFile(photoS3Name);
        photoRepository.deleteUploadedPhoto(photoS3Name);


    }
}
