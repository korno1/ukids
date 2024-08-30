package com.modernfamily.ukids.domain.game.quiz.controller;

import com.modernfamily.ukids.domain.game.quiz.model.service.QuizService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;
    private final SimpMessagingTemplate messagingTemplate;


    // 게임방 생성 -> 게임방 정보 및 connection 반환
    @MessageMapping("/quiz/enter")
    public void enterQuizRoom(@RequestBody Map<String, Object> payload,
                                             SimpMessageHeaderAccessor headerAccessor)
            throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Received payload: {}", payload);

        String userId = headerAccessor.getUser().getName();
        log.info("Entering quiz room : {}", userId);

        Long familyId = Long.parseLong(payload.get("familyId").toString());

        messagingTemplate.convertAndSend("/topic/quiz/" + familyId, quizService.enterQuizRoom(familyId, userId));

    }

    // 유저 퇴장
    @MessageMapping("/quiz/exit")
    public void exitQuizRoom(@RequestBody Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        Long familyId = Long.parseLong(payload.get("familyId").toString());

        messagingTemplate.convertAndSend("/topic/quiz/" + familyId, quizService.exitQuizRoom(familyId, userId));

    }

    // 게임방 정보 반환
    @MessageMapping("/quiz/info")
    public void getQuizRoom(@RequestBody Map<String, Object> payload) {
        Long familyId = Long.parseLong(payload.get("familyId").toString());

        messagingTemplate.convertAndSend("/topic/quiz/" + familyId,quizService.getQuizRoom(familyId));

    }

    // 최대 퀴즈 개수 반환
    @MessageMapping("/quiz/quiz-max")
    public void updateMaxQuestionCounts(@RequestBody Map<String, Object> payload) {
        Long familyId = Long.parseLong(payload.get("familyId").toString());

        messagingTemplate.convertAndSend("/topic/quiz/" + familyId,quizService.updateMaxQuestionCounts(familyId));

    }

    // 퀴즈 개수 설정
    @MessageMapping("/quiz/quiz-count")
    public void saveQuizCounts(@RequestBody Map<String, Object> payload) {
        Long familyId = Long.parseLong(payload.get("familyId").toString());
        Long counts = Long.parseLong(payload.get("counts").toString());

        messagingTemplate.convertAndSend("/topic/quiz/" + familyId, quizService.saveQuizCounts(familyId, counts));
    }

    // 준비 클릭 -> 다 준비되면 바로 게임 시작 -> 퀴즈 개수만큼 퀴즈 생성 => boolean return
    @MessageMapping("/quiz/ready")
    public void isReadyGameStart(@RequestBody Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        Long familyId = Long.parseLong(payload.get("familyId").toString());
        boolean isReady = payload.get("state").toString().equals("true");

        Map<String, Object> quizRoom = quizService.isReadyGameStart(familyId, userId, isReady);
        messagingTemplate.convertAndSend("/topic/quiz/" + familyId, quizRoom);
    }

    @MessageMapping("/quiz/question")
    public void getQuestion(@RequestBody Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
        String userId = headerAccessor.getUser().getName();
        Long familyId = Long.parseLong(payload.get("familyId").toString());

        Map<String, Object> question = quizService.getQuizQuestion(familyId, userId);

        if(question != null) {
            TimeUnit.SECONDS.sleep(3);
            messagingTemplate.convertAndSend("/topic/quiz/" + familyId, question);
        }
    }

    // 정답 확인
    @MessageMapping("/quiz/answer")
    public void checkQuizAnswer(@RequestBody Map<String, Object> payload,
                                               SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        Long familyId = Long.parseLong(payload.get("familyId").toString());
        // messagingTemplate.convertAndSend("/topic/quiz/" + familyId, quizService.checkQuizAnswer(familyId, (String) payload.get("inputAnswer"), userId));
        quizService.checkQuizAnswer(familyId, (String) payload.get("inputAnswer"), userId);
    }

    @MessageMapping("/quiz/quit")
    public void deleteQuizRoom(@RequestBody Map<String, Object> payload,
                                SimpMessageHeaderAccessor headerAccessor) {
        Long familyId = Long.parseLong(payload.get("familyId").toString());
        quizService.deleteQuizRoom(familyId);

    }
}
