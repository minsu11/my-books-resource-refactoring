package store.mybooks.resource.return_rule.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.return_rule.dto.mapper.ReturnRuleMapper;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleCreateRequest;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleModifyRequest;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleCreateResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleModifyResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.entity.ReturnRule;
import store.mybooks.resource.return_rule.exception.ReturnRuleAlreadyExistException;
import store.mybooks.resource.return_rule.exception.ReturnRuleNotExistException;
import store.mybooks.resource.return_rule.repository.ReturnRuleRepository;
import store.mybooks.resource.return_rule_name.entity.ReturnRuleName;
import store.mybooks.resource.return_rule_name.exception.ReturnRuleNameAlreadyExistException;
import store.mybooks.resource.return_rule_name.exception.ReturnRuleNameNotExistException;
import store.mybooks.resource.return_rule_name.repository.ReturnRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.return_rule.service<br>
 * fileName       : ReturnRuleService<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 반품 정책 등록, 수정, 삭제, 조회를 담당하는 서비스
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReturnRuleService {
    private final ReturnRuleRepository returnRuleRepository;
    private final ReturnRuleNameRepository returnRuleNameRepository;

    private final ReturnRuleMapper returnRuleMapper;


    /**
     * methodName : getReturnRuleResponseByReturnRuleName<br>
     * author : minsu11<br>
     * description : <br>
     * {@code returnRuleName}의 반품 정책 조회. <br>
     * {@code returnRuleName}로 반품 정책을 조회 할 수 없는 경우 {@code ReturnRuleNotExistException}을 던짐
     * <br> *
     *
     * @param returnRuleName 조회할 반품 규정 이름
     * @return returnRuleResponse dto로 반환
     * @throws ReturnRuleNotExistException {@code returnRuleName}의 데이터를 조회 할 수 없는 경우
     */
    @Transactional(readOnly = true)
    public ReturnRuleResponse getReturnRuleResponseByReturnRuleName(String returnRuleName) {
        return returnRuleRepository
                .findByReturnRuleName(returnRuleName)
                .orElseThrow(() -> new ReturnRuleNotExistException("반품 규정이 존재하지 않음"));
    }

    /**
     * methodName : getReturnRuleResponseList<br>
     * author : minsu11<br>
     * description : 모든 반품 정책 데이터를 조회. DB에 존재 하지 않는 경우 빈 리스트를 반환.
     * <br> *
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<ReturnRuleResponse> getReturnRuleResponseList() {
        return returnRuleRepository.getReturnRuleResponseList();
    }

    /**
     * methodName : createReturnRule<br>
     * author : minsu11<br>
     * description : 반품 증록 정책 요청이 들어 왔을 때 {@code request}의 정보를 저장
     * 저장 하기 전 {@code request}의 {@code name}으로 데이터를 조회. <br>
     * 조회 후
     * 조회 데이터가 있으면 {@code ReturnRuleAlreadyExistException}를 던짐.
     *
     * <br> *
     *
     * @param request 등록 할 반품 규정 명 데이터
     * @return returnRuleCreateResponse
     */
    public ReturnRuleCreateResponse createReturnRule(ReturnRuleCreateRequest request) {
        if (returnRuleRepository.findByReturnRuleName(request.getReturnName()).isPresent()) {
            throw new ReturnRuleAlreadyExistException("해당하는 반품 규정 명 존재");
        }

        ReturnRuleName returnRuleName =
                returnRuleNameRepository
                        .findById(request.getReturnName())
                        .orElseThrow(() -> new ReturnRuleNameAlreadyExistException("이미 존재하는 반품 정책 명입니다."));

        ReturnRule returnRule =
                new ReturnRule(1, request.getDeliveryFee(), request.getTerm(), true, LocalDate.now(), returnRuleName);

        returnRuleRepository.save(returnRule);

        return returnRuleMapper.mapToReturnRuleCreateResponse(returnRule);
    }

    /**
     * methodName : modifyReturnRule<br>
     * author : minsu11<br>
     * description : {@code returnRuleName}에 따른 정책 명을 조회 한 후,
     * 정책 명으로 정책을 조회. 조회한 정책을 {@code request} 데이터로 수정.
     * 반품 정책 명과 반품 정책 조회 시 데이터가 존재하지 않는 경우
     * {@code ReturnRuleNameNotExistException}, {@code ReturnRuleNotExistException}을 던져줌
     * <br>
     *
     * @param request        수정될 반품 규정
     * @param returnRuleName 수정할 반품 규정 명
     * @return returnRuleModifyResponse
     * @throws ReturnRuleNameNotExistException 반품 규정 명이 존재하지 않을 때
     * @throws ReturnRuleNotExistException     반품 규정이 존재 하지 않을 때
     */
    public ReturnRuleModifyResponse modifyReturnRule(ReturnRuleModifyRequest request, String returnRuleName) {

        ReturnRuleName returnRuleNameResponse =
                returnRuleNameRepository
                        .findById(returnRuleName)
                        .orElseThrow(() -> new ReturnRuleNameNotExistException("반품 정책 명이 존재하지 않음"));
        ReturnRule returnRule =
                returnRuleRepository
                        .findByReturnRuleNameId(returnRuleNameResponse.getId())
                        .orElseThrow(() -> new ReturnRuleNotExistException("존재하지 않음"));

        returnRule.modifyByReturnRule(request, returnRuleNameResponse);
        return returnRuleMapper.mapToReturnRuleModifyResponse(returnRule);
    }


}
