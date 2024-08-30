package com.modernfamily.ukids.domain.album.dto.response;

import com.modernfamily.ukids.domain.album.entity.Album;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class FamilyAlbumListResponseDto {

    Long albumId;

    String title;

    LocalDate date;

    private FamilyAlbumListResponseDto(Long albumId, String title, LocalDate date) {
        this.albumId = albumId;
        this.title = title;
        this.date = date;
    }

    public static FamilyAlbumListResponseDto createResponseDto(Album album) {
        log.info("album 생성 즁 : { }", album.getAlbumId());
        return new FamilyAlbumListResponseDto(album.getAlbumId(), album.getTitle(), album.getDate());
    }

}
