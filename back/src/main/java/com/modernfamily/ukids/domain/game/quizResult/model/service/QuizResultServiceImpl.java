package com.modernfamily.ukids.domain.game.quizResult.model.service;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultPaginationResponseDto;
import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultResponseDto;
import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultSaveDto;
import com.modernfamily.ukids.domain.game.quizResult.entity.QuizResult;
import com.modernfamily.ukids.domain.game.quizResult.model.repository.QuizResultRepository;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.domain.user.model.repository.UserRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
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
public class QuizResultServiceImpl implements QuizResultService {

    private final QuizResultRepository quizResultRepoitory;
    private final FamilyMemberRepository familyMemberRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void saveGameResult(List<QuizResultSaveDto> gameResultSaveDtoList) {
        List<QuizResult> gameResultList = new ArrayList<>();

        for (QuizResultSaveDto gameResultSaveDto : gameResultSaveDtoList) {
            FamilyMember familyMember = familyMemberRepository.findByUser_IdAndFamily_FamilyId
                    (gameResultSaveDto.getParticipantId(), gameResultSaveDto.getFamilyId())
                    .orElseThrow(()->new ExceptionResponse(CustomException.NOT_FOUND_FAMILYMEMBER_EXCEPTION));
            gameResultList.add(QuizResult.createGameResult(gameResultSaveDto, familyMember.getUser(), familyMember.getFamily()));
        }

        quizResultRepoitory.saveAll(gameResultList);
    }

    public QuizResultPaginationResponseDto getQuizResults(int size, int page) {
        String userId = CustomUserDetails.contextGetUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Pageable pageable = PageRequest.of(--page, size);
        Page<QuizResult> quizResultsPage = quizResultRepoitory.findByParticipant_UserId(user.getUserId(), pageable);

        List<QuizResult> quizResultsList = quizResultsPage.getContent();
        List<QuizResultResponseDto> responseDtoList = new ArrayList<>();

        for (QuizResult quizResult : quizResultsList) {
            responseDtoList.add(QuizResultResponseDto.createResponseDto(quizResult));
        }

        QuizResultPaginationResponseDto pagenationResponseDto = QuizResultPaginationResponseDto
                .createResponseDto(quizResultsPage.getNumberOfElements(), quizResultsPage.getNumber()+1,
                        quizResultsPage.getTotalPages(), quizResultsPage.getTotalElements(), responseDtoList, userMapper.toUserDto(user));

        return pagenationResponseDto;
    }


}
