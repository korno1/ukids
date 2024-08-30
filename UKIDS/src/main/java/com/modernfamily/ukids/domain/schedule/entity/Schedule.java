package com.modernfamily.ukids.domain.schedule.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_delete = false")
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "title", nullable = false, length = 300)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "place", length = 300)
    private String place;

    @Column(name = "start_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_delete", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @Builder
    private Schedule(String title, String content, String place, LocalDateTime startTime, LocalDateTime endTime, boolean isDelete, Family family) {
        this.title = title;
        this.content = content;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isDelete = isDelete;
        this.family = family;
    }

    public static Schedule createSchedule(String title, String content, String place, LocalDateTime startTime, LocalDateTime endTime, Family family) {
        return Schedule.builder()
                .title(title)
                .content(content)
                .place(place)
                .startTime(startTime)
                .endTime(endTime)
                .isDelete(false)
                .family(family)
                .build();
    }

    public void updateSchedule(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void deleteSchedule() {
        this.isDelete = true;
    }

}
