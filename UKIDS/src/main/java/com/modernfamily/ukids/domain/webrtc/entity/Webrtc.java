package com.modernfamily.ukids.domain.webrtc.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Webrtc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long webrtcId;

    @Column(nullable = false)
    private String sessionId;

    @OneToOne
    @JoinColumn(name = "family_id")
    private Family family;

    public Webrtc(String sessionId, Family family) {
        this.sessionId = sessionId;
        this.family = family;
    }

}
