package com.modernfamily.ukids.domain.album.dto.response;

import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.mapper.FamilyMapper;
import com.modernfamily.ukids.domain.user.dto.UserFamilyDto;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumInfoResponseDto {

    Long albumId;

    String title;

    LocalDate date;

    FamilyResponseDto family;

    @Builder
    private AlbumInfoResponseDto(Long albumId, String title, LocalDate date, FamilyResponseDto family) {
        this.albumId = albumId;
        this.title = title;
        this.date = date;
        this.family = family;
    }

    public static AlbumInfoResponseDto createAlbumInfoResponseDto(Album album, FamilyResponseDto familyResponseDto) {
        return AlbumInfoResponseDto.builder()
                .albumId(album.getAlbumId())
                .title(album.getTitle())
                .date(album.getDate())
                .family(familyResponseDto)
                .build();
    }

}
