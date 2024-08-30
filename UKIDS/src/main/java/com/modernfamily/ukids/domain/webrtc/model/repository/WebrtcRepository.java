package com.modernfamily.ukids.domain.webrtc.model.repository;

import com.modernfamily.ukids.domain.webrtc.entity.Webrtc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebrtcRepository extends JpaRepository<Webrtc, Long> {
    Optional<Webrtc> findByFamily_FamilyId(Long familyId);
}
