package com.modernfamily.ukids.domain.album.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Getter
@Where(clause = "is_delete = false")
public class Album{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "is_delete", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    private Album(LocalDate date, String title, boolean isDelete, Family family){
        this.date = date;
        this.title = title;
        this.isDelete = isDelete;
        this.family = family;
    }

    public static Album createAlbum(LocalDate date, String title, Family family){
        return new Album(date, title, false,  family);
    }

    public void updateAlbum(Long albumId){
        this.albumId = albumId;
    }

    public void deleteAlbum(){
        this.isDelete = true;
    }
}
