package com.modernfamily.ukids.domain.webrtc.model.service;

import com.modernfamily.ukids.domain.webrtc.dto.response.WebrtcResponseDto;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

import java.util.Map;

public interface WebrtcService {
    void init();
    String initializeSessions(Map<String, Object> sessionProperties) throws OpenViduJavaClientException, OpenViduHttpException;
    String createConnection(Long familyId, Map<String, Object> connectionProperties) throws OpenViduJavaClientException, OpenViduHttpException;
    void createWebrtcChatRoom(String sessionId, Long familyId);
    WebrtcResponseDto getWebrtcByFamilyId(Long familyId);
    String getToken(String sessionId, Map<String, Object> connectionProperties) throws OpenViduJavaClientException, OpenViduHttpException;
}
