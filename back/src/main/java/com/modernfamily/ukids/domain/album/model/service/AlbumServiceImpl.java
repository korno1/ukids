package com.modernfamily.ukids.domain.album.model.service;

import com.modernfamily.ukids.domain.album.dto.request.AlbumCreateRequestDto;
import com.modernfamily.ukids.domain.album.dto.response.FamilyAlbumListResponseDto;
import com.modernfamily.ukids.domain.album.dto.response.AlbumInfoResponseDto;
import com.modernfamily.ukids.domain.album.dto.request.AlbumUpdateRequestDto;
import com.modernfamily.ukids.domain.album.dto.response.FamilyAlbumPagenationResponseDto;
import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.album.model.repository.AlbumRepository;
import com.modernfamily.ukids.domain.caption.model.repository.CaptionRepository;
import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.mapper.FamilyMapper;
import com.modernfamily.ukids.domain.photo.model.repository.PhotoRepository;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final FamilyMemberValidator familyMemberValidator;
    private final PhotoRepository photoRepository;
    private final CaptionRepository captionRepository;
    private final FamilyMapper familyMapper;
    private final UserMapper userMapper;

    @Transactional
    public void createAlbum(AlbumCreateRequestDto requestDto) {

        Family family = familyMemberValidator.checkUserInFamilyMember(requestDto.getFamilyId()).getFamily();

        Optional<Album> existAlbum =  albumRepository.findByDateAndFamily_FamilyId(requestDto.getDate(), requestDto.getFamilyId());
        if(!existAlbum.isEmpty())
            throw new ExceptionResponse(CustomException.DUPLICATED_ALBUM_EXCEPTION);

        Album album = Album.createAlbum(requestDto.getDate(), requestDto.getTitle(), family);

        albumRepository.save(album);
    }

    @Transactional
    public void updateAlbum(AlbumUpdateRequestDto requestDto) {
        Family family = familyMemberValidator.checkUserInFamilyMember(requestDto.getFamilyId()).getFamily();

        albumRepository.findByAlbumId(requestDto.getAlbumId()).orElseThrow(() ->
            new ExceptionResponse(CustomException.NOT_FOUND_ALBUM_EXCEPTION));

        Optional<Album> existAlbum = albumRepository.findByDateAndFamily_FamilyId(requestDto.getDate(), requestDto.getFamilyId());
        if(!existAlbum.isEmpty() && existAlbum.get().getAlbumId() != requestDto.getAlbumId())
            throw new ExceptionResponse(CustomException.DUPLICATED_ALBUM_EXCEPTION);

        Album album = Album.createAlbum(requestDto.getDate(), requestDto.getTitle(), family);
        album.updateAlbum(requestDto.getAlbumId());

        albumRepository.save(album);

    }

    public AlbumInfoResponseDto getAlbumInfo(Long albumId) {

        Album album = albumRepository.findByAlbumId(albumId).orElseThrow(() ->
                new ExceptionResponse(CustomException.NOT_FOUND_ALBUM_EXCEPTION));

        familyMemberValidator.checkUserInFamilyMember(album.getFamily().getFamilyId());

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(album.getFamily());
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(album.getFamily().getUser()));

        AlbumInfoResponseDto responseDto = AlbumInfoResponseDto.createAlbumInfoResponseDto(album, familyResponseDto);
        return responseDto;
    }

    public FamilyAlbumPagenationResponseDto getAlbumInfoList(int size, int page, Long familyId) {

        Family family = familyMemberValidator.checkUserInFamilyMember(familyId).getFamily();

        Pageable pageable = PageRequest.of(--page, size);
        Page<Album> albumPage = albumRepository.findAllByFamily_FamilyIdOrderByDateDesc(familyId, pageable);

        List<Album> albumList = albumPage.getContent();
        List<FamilyAlbumListResponseDto> responseDtoList = new ArrayList<>();
        log.info("Album List size : {}", albumList.size());
        for (Album album : albumList) {
            responseDtoList.add(FamilyAlbumListResponseDto.createResponseDto(album));
        }

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));

        FamilyAlbumPagenationResponseDto pagenationResponseDto = FamilyAlbumPagenationResponseDto
                .createResponseDto(responseDtoList, albumPage.getNumberOfElements(), albumPage.getNumber()+1, albumPage.getTotalPages(), familyResponseDto);

        return pagenationResponseDto;
    }

    @Transactional
    public void deleteAlbum(Long albumId){
        // 비밀번호 확인 가정

        Album album = albumRepository.findByAlbumId(albumId).orElseThrow(() ->
                new ExceptionResponse(CustomException.NOT_FOUND_ALBUM_EXCEPTION));

        familyMemberValidator.checkUserInFamilyMember(album.getFamily().getFamilyId());

        // captionRepository.deleteAllByAlbum(album); // 추후 기능 구현 예정
        photoRepository.deleteAllByAlbum(album);

        album.deleteAlbum();
        albumRepository.save(album);
    }


}


