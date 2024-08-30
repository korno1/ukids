package com.modernfamily.ukids.domain.webrtc.dto.response;

import com.modernfamily.ukids.domain.webrtc.entity.Webrtc;
import lombok.Getter;

@Getter
public class WebrtcResponseDto {

    private String sessionId;

    private Long familyId;

    private WebrtcResponseDto(String sessionId, Long familyId) {
        this.sessionId = sessionId;
        this.familyId = familyId;
    }

    public static WebrtcResponseDto from(Webrtc webrtc) {
        return new WebrtcResponseDto(
                webrtc.getSessionId(),
                webrtc.getFamily().getFamilyId()
        );
    }
}
