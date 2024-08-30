package com.modernfamily.ukids.domain.webrtc.model.service;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.webrtc.dto.response.WebrtcResponseDto;
import com.modernfamily.ukids.domain.webrtc.entity.Webrtc;
import com.modernfamily.ukids.domain.webrtc.model.repository.WebrtcRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WebrtcServiceImpl implements WebrtcService {

    @Value("${webrtc.openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${webrtc.openvidu.secret}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;
    private final WebrtcRepository webrtcRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMemberValidator familyMemberValidator;

    @Override
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Override
    public String initializeSessions(Map<String, Object> sessionProperties) throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(sessionProperties).build();
        Session session = openvidu.createSession(properties);

        return session.getSessionId();
    }

    @Override
    public String createConnection(Long familyId, Map<String, Object> connectionProperties) throws OpenViduJavaClientException, OpenViduHttpException {
        familyMemberValidator.checkUserInFamilyMember(familyId);
        
        String sessionId = getWebrtcByFamilyId(familyId).getSessionId();
        return getToken(sessionId, connectionProperties);
    }

    @Override
    public String getToken(String sessionId, Map<String, Object> connectionProperties) throws OpenViduJavaClientException, OpenViduHttpException {

        openvidu.fetch();

        List<Session> activeSessionList = openvidu.getActiveSessions();

        Optional<Session> sessionOptional = activeSessionList.stream()
                .filter(s -> s.getSessionId().equals(sessionId))
                .findFirst();

        Session session = sessionOptional.isPresent()? sessionOptional.get()
                : openvidu.createSession(new SessionProperties.Builder().customSessionId(sessionId).build());

        ConnectionProperties properties = ConnectionProperties.fromJson(connectionProperties).build();
        Connection connection = session.createConnection(properties);

        return connection.getToken();
    }

    @Override
    public void createWebrtcChatRoom(String sessionId, Long familyId) {
        Family family = familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        Webrtc webrtc = new Webrtc(sessionId, family);

        webrtcRepository.save(webrtc);
    }

    @Override
    public WebrtcResponseDto getWebrtcByFamilyId(Long familyId) {
        familyMemberValidator.checkUserInFamilyMember(familyId);
        Webrtc webrtc = webrtcRepository.findByFamily_FamilyId(familyId).get();

        return WebrtcResponseDto.from(webrtc);
    }
}
