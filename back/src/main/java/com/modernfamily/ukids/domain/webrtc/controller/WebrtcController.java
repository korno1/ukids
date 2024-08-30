package com.modernfamily.ukids.domain.webrtc.controller;

import com.modernfamily.ukids.domain.webrtc.model.service.WebrtcService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/webrtc")
@RequiredArgsConstructor
public class WebrtcController {

    private final HttpResponseUtil responseUtil;
    private final WebrtcService webrtcService;

    @PostConstruct
    public void init() {
        webrtcService.init();
    }

    @PostMapping
    public ResponseEntity<?> initializeSessions(@RequestBody(required = false) Map<String, Object> sessionProperties,
                                                @RequestParam("familyId") Long familyId) throws OpenViduJavaClientException, OpenViduHttpException {
        String sessionId = webrtcService.initializeSessions(sessionProperties);

        webrtcService.createWebrtcChatRoom(sessionId, familyId);

        return responseUtil.createResponse(HttpMethodCode.POST, sessionId);
    }

    @PostMapping("/{familyId}")
    public ResponseEntity<?> createConnection(@PathVariable("familyId") Long familyId,
                                              @RequestBody(required = false) Map<String, Object> connectionProperties)
            throws OpenViduJavaClientException, OpenViduHttpException {

        String token = webrtcService.createConnection(familyId, connectionProperties);

        return responseUtil.createResponse(HttpMethodCode.POST, token);
    }

}
