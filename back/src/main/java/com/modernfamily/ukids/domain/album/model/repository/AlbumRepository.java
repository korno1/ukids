package com.modernfamily.ukids.domain.album.model.repository;

import com.modernfamily.ukids.domain.album.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByDateAndFamily_FamilyId(LocalDate date, Long familyId);

    Optional<Album> findByAlbumId(Long albumId);

    Page<Album> findAllByFamily_FamilyIdOrderByDateDesc(Long familyId, Pageable pageable);
}
