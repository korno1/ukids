package com.modernfamily.ukids.domain.growthFolder.model.repository;

import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderRequestDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderUpdateRequestDto;
import com.modernfamily.ukids.domain.growthFolder.entity.GrowthFolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GrowthFolderRepository extends JpaRepository<GrowthFolder, Long> {

    Optional<GrowthFolder> findByFolderId(Long folderId);

    Page<GrowthFolder> findAllByFamily_FamilyId(Long familyId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update GrowthFolder gf " +
            "set gf.folderName = :#{#growthFolder.folderName}, " +
            "gf.updateTime = now() " +
            "where gf.folderId = :#{#growthFolder.folderId}")
    void updateGrowthFolder(GrowthFolder growthFolder);

    @Modifying
    @Transactional
    @Query(value = "update GrowthFolder gf " +
            "set gf.isDelete = true, " +
            "gf.updateTime = now() " +
            "where gf.folderId = :folderId")
    void deleteGrowthFolder(Long folderId);
}
