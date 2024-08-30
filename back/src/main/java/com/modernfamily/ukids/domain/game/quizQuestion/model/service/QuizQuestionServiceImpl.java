package com.modernfamily.ukids.domain.game.quizQuestion.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernfamily.ukids.domain.game.chatgpt.service.ChatgptService;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.request.QuizQuestionCreateRequestDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.request.QuizQuestionUpdateRequestDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionListPagenationResponseDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionListResponseDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionRandomResponseDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionResponseDto;
import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizQuestion;
import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizType;
import com.modernfamily.ukids.domain.game.quizQuestion.model.repository.QuizQuestionRepository;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.domain.user.model.repository.UserRepository;
import com.modernfamily.ukids.global.config.ChatgptConfig;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final UserRepository userRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final UserMapper userMapper;
    private final ChatgptService chatgptService;


    @Transactional
    public void createQuizQuestion(QuizQuestionCreateRequestDto requestDto) {
        String id = CustomUserDetails.contextGetUserId();
        User writer = userRepository.findById(id).orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        QuizQuestion quizQuestion = QuizQuestion.createQuizQuestion(requestDto.getQuestion(), requestDto.getAnswer(),
                requestDto.getQuizType(), writer);

        quizQuestionRepository.save(quizQuestion);
    }

    @Transactional
    public void updateQuizQuestion(QuizQuestionUpdateRequestDto requestDto) {
        String id = CustomUserDetails.contextGetUserId();
        User writer = userRepository.findById(id).orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        QuizQuestion existQuizQuestion = quizQuestionRepository.findByQuizQuestionId(requestDto.getQuizQuestionId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_QUIZ_EXCEPTION));

        if(writer.getUserId() != existQuizQuestion.getWriter().getUserId())
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);

        QuizQuestion quizQuestion = QuizQuestion.createQuizQuestion(requestDto.getQuestion(), requestDto.getAnswer(),
                requestDto.getQuizType(), writer);

        quizQuestion.updateQuizQuestion(requestDto.getQuizQuestionId());

        quizQuestionRepository.save(quizQuestion);
    }

    @Transactional
    public void deleteQuizQuestion(Long quizQuestionId) {
        String id = CustomUserDetails.contextGetUserId();
        User writer = userRepository.findById(id).orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        QuizQuestion quizQuestion = quizQuestionRepository.findByQuizQuestionId(quizQuestionId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_QUIZ_EXCEPTION));

        quizQuestion.deleteQuizQuestion();

        quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestionResponseDto getQuizQuestion(Long quizQuestionId) {
        String id = CustomUserDetails.contextGetUserId();
        User writer = userRepository.findById(id).orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        QuizQuestion quizQuestion = quizQuestionRepository.findByQuizQuestionId(quizQuestionId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_QUIZ_EXCEPTION));

        return QuizQuestionResponseDto.createResponseDto(quizQuestion, userMapper.toUserDto(writer));
    }

    public QuizQuestionListPagenationResponseDto getQuizQuestionListByUser(int size, int page) {
        String id = CustomUserDetails.contextGetUserId();
        User writer = userRepository.findById(id).orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Pageable pageable = PageRequest.of(--page, size);
        Page<QuizQuestion> quizQuestionsPage = quizQuestionRepository.findByWriter_UserId(writer.getUserId(), pageable);

        List<QuizQuestion> quizQuestionList = quizQuestionsPage.getContent();
        List<QuizQuestionListResponseDto> responseDtoList = new ArrayList<>();

        for (QuizQuestion quizQuestion : quizQuestionList) {
            responseDtoList.add(QuizQuestionListResponseDto.createResponseDto(quizQuestion));
        }

        QuizQuestionListPagenationResponseDto pagenationResponseDto = QuizQuestionListPagenationResponseDto
                .createResponseDto(quizQuestionsPage.getNumberOfElements(), quizQuestionsPage.getNumber()+1,
                        quizQuestionsPage.getTotalPages(), quizQuestionsPage.getTotalElements(), responseDtoList, userMapper.toUserDto(writer));

        return pagenationResponseDto;
    }

    public long getCountQuizQuestionByUser(String id) {

        return quizQuestionRepository.countByWriter_Id(id);
    }

    public List<QuizQuestionRandomResponseDto> chooseRandomQuizQuestion(String userId , long count) {
        User writer = userRepository.findById(userId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        long existCount = quizQuestionRepository.countByWriter_Id(userId);
        if(existCount < count)
            throw new ExceptionResponse(CustomException.NOT_ENOUGH_QUIZ_QUESTION_EXCEPTION);

        List<QuizQuestion> quizQuestions = quizQuestionRepository.findRandomQuizQuestionsByUser(count, writer.getUserId());
        List<QuizQuestionRandomResponseDto> responseDtoList = new ArrayList<>();

        for (QuizQuestion quizQuestion : quizQuestions) {
            List<String> wrongAnswer = null;

            if(quizQuestion.getQuizType() == QuizType.MULTIPLE_CHOICE){
                String prompt = quizQuestion.getQuestion() + " 에 대한 답변이 " + quizQuestion.getAnswer() + "인데, 문제에 대한 다른 보기 2개만 줘. 단어면 단어로. '보기', 번호 이런거는 빼도 됨";
                wrongAnswer = chatgptService.runPrompt(prompt);
            }
            responseDtoList.add(QuizQuestionRandomResponseDto.createResponseDto(quizQuestion, userMapper.toUserDto(writer), wrongAnswer));
        }

        return responseDtoList;
    }


}
