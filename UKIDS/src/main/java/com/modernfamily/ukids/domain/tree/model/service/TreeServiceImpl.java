package com.modernfamily.ukids.domain.tree.model.service;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.letter.model.repository.LetterRepository;
import com.modernfamily.ukids.domain.tree.dto.request.TreeCreateRequestDto;
import com.modernfamily.ukids.domain.tree.dto.request.TreeUpdateRequestDto;
import com.modernfamily.ukids.domain.tree.dto.response.TreeInfoResponseDto;
import com.modernfamily.ukids.domain.tree.entity.Tree;
import com.modernfamily.ukids.domain.tree.model.repository.TreeRepository;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;
    private final FamilyRepository familyRepository;
    private final LetterRepository letterRepository;

    @Override
    public Tree createTree(TreeCreateRequestDto treeDto) {
        // familyId로 Family 엔티티 조회
        Family family = familyRepository.findByFamilyId(treeDto.getFamilyId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        Tree tree = new Tree(family);
        tree.setFamily(family);

        return treeRepository.save(tree);
    }

    // 가족 id를 통한 현재 나무 조회
    @Override
    public TreeInfoResponseDto findByFamilyId(Long familyId) {
        // familyId로 Tree 엔티티 조회
        Tree tree = treeRepository.findByFamily_FamilyIdAndIsCompleteFalse(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_TREE_EXCEPTION));

        // 해당 나무와 연결된 편지 수 계산
        long letterCount = letterRepository.countByTree_TreeId(tree.getTreeId()); // isOpen이 false인 편지 개수

        return TreeInfoResponseDto.createResponseDto(tree, letterCount);
    }

    // 가족 id를 통해 나무 경험치 업데이트
    @Override
    public Tree updateTree(TreeUpdateRequestDto treeDto) {
        // familyId로 Tree 엔티티 조회
        Tree tree = treeRepository.findByFamily_FamilyIdAndIsCompleteFalse(treeDto.getFamilyId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_TREE_EXCEPTION));

        // point를 exp에 저장
        Long grownExp = tree.getExp() + treeDto.getPoint();

        // 성장 exp 가 최대치를 넘는지 안넘는지 체크
        if (grownExp >= 1000) {
            tree.setIsComplete(true);

            // Tree 가 완성된 경우 관련된 모든 편지의 isOpen을 true로 업데이트
            letterRepository.updateLettersOpenStatusByTree(tree);

            TreeCreateRequestDto newTreeDto = new TreeCreateRequestDto(tree.getFamily().getFamilyId());
            createTree(newTreeDto);
        } else {
            tree.setExp(grownExp);
        }

        return treeRepository.save(tree);
    }

}
