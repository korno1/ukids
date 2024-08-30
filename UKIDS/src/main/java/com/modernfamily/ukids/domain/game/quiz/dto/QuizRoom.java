package com.modernfamily.ukids.domain.game.quiz.dto;

import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionRandomResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class QuizRoom {
    private String sessionId;
    private Long quizCount;
    private boolean isStart;
    private Long saveCounts;
    private String hostId;
    private Long numberOfParticipants;
    private Long maxQuestionCounts;
    private int currentQuestionIndex;
    private HashMap<String, Participate> participantList;
    private List<QuizQuestionRandomResponseDto> randomQuizQuestionList;

    @Builder
    private QuizRoom(String sessionId ,Long quizCount, boolean isStart, Long saveCounts, String hostId,
                     Long numberOfParticipants, Long maxQuestionCounts, int currentQuestionIndex,
                     HashMap<String, Participate> participantList, List<QuizQuestionRandomResponseDto> randomQuizQuestionList) {
        this.sessionId = sessionId;
        this.quizCount = quizCount;
        this.isStart = isStart;
        this.saveCounts = saveCounts;
        this.hostId = hostId;
        this.numberOfParticipants = numberOfParticipants;
        this.maxQuestionCounts = maxQuestionCounts;
        this.currentQuestionIndex = currentQuestionIndex;
        this.participantList = participantList;
        this.randomQuizQuestionList = randomQuizQuestionList;
    }

    public static QuizRoom createQuizRoom(String sessionId){
        return QuizRoom.builder()
                .sessionId(sessionId)
                .quizCount(0L)
                .isStart(false)
                .saveCounts(0L)
                .hostId(null)
                .numberOfParticipants(0L)
                .maxQuestionCounts(Long.MAX_VALUE)
                .currentQuestionIndex(-1)
                .participantList(new HashMap<String, Participate>())
                .randomQuizQuestionList(new ArrayList<>())
                .build();

    }

    public void changeHost(String hostId){
        this.hostId = hostId;
    }

    public void nextHost(String hostId){
        for(Map.Entry<String, Participate> entry : participantList.entrySet()){
            if(entry.getKey().equals(hostId)) continue;
            this.hostId = entry.getKey();
            break;
        }
    }

    public void saveGame(){
        this.saveCounts++;
    }

    public void updateQuizCount(long quizCount){
        this.quizCount = quizCount;
    }

    public void updateMaxQuestionCounts(){
        this.maxQuestionCounts = Long.MAX_VALUE;
        for(Map.Entry<String, Participate> entry : participantList.entrySet()){
            Participate participate = entry.getValue();
            this.maxQuestionCounts = Math.min(maxQuestionCounts, participate.getMaxQuestion());
        }
    }

    public void startGame(){
        this.isStart = true;
        this.saveCounts = 0L;
        this.numberOfParticipants = (long) participantList.size();
    }

    public void endGame(){
        this.isStart = false;
    }

    public void enterParticipate(String userId, Participate participant){
        participantList.put(userId, participant);
        log.info("Enter user : {}", participant.getUserName());
        this.maxQuestionCounts = Math.min(maxQuestionCounts, participant.getMaxQuestion());
        log.info("User's Max question count : {}", participant.getMaxQuestion());
        ++this.numberOfParticipants;
    }

    public void exitParticipate(String userId){
        long exitQuestionCount = participantList.get(userId).getMaxQuestion();
        participantList.remove(userId);

        if(maxQuestionCounts == exitQuestionCount){
              for(Participate participant : participantList.values()){
                  maxQuestionCounts = Math.min(maxQuestionCounts, participant.getMaxQuestion());
              }
        }
        --this.numberOfParticipants;
    }

    public void initQuizList(){
        this.currentQuestionIndex = -1;
        this.randomQuizQuestionList.clear();
    }

    public void generateRandomQuizQuestion(List<QuizQuestionRandomResponseDto> randomQuizQuestionList){

        this.randomQuizQuestionList.addAll(randomQuizQuestionList);
    }

    public boolean increaseCurrentQuestionIndex(){
        if(randomQuizQuestionList.size()-1 == this.currentQuestionIndex)
            return false;

        this.currentQuestionIndex++;
        return true;
    }

}
