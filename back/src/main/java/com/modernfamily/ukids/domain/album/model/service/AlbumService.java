package com.modernfamily.ukids.domain.album.model.service;

import com.modernfamily.ukids.domain.album.dto.request.AlbumCreateRequestDto;
import com.modernfamily.ukids.domain.album.dto.response.FamilyAlbumListResponseDto;
import com.modernfamily.ukids.domain.album.dto.response.AlbumInfoResponseDto;
import com.modernfamily.ukids.domain.album.dto.request.AlbumUpdateRequestDto;
import com.modernfamily.ukids.domain.album.dto.response.FamilyAlbumPagenationResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;

import java.util.List;

public interface AlbumService {

    void createAlbum(AlbumCreateRequestDto requestDto);

    void updateAlbum(AlbumUpdateRequestDto requestDto);

    AlbumInfoResponseDto getAlbumInfo(Long albumId);

    FamilyAlbumPagenationResponseDto getAlbumInfoList(int size, int page, Long familyId);

    void deleteAlbum(Long albumId);
}
