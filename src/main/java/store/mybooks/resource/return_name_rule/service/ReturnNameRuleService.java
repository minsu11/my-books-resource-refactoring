package store.mybooks.resource.return_name_rule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.return_name_rule.dto.mapper.ReturnNameRuleMapper;
import store.mybooks.resource.return_name_rule.dto.response.ReturnNameRuleResponse;
import store.mybooks.resource.return_name_rule.entity.ReturnNameRule;
import store.mybooks.resource.return_name_rule.exception.ReturnNameRuleNotFoundException;
import store.mybooks.resource.return_name_rule.repository.ReturnNameRuleRepository;

/**
 * packageName    : store.mybooks.resource.return_name_rule.service
 * fileName       : ReturnNameRuleService
 * author         : minsu11
 * date           : 2/20/24
 * description    : 반품 규정 명 규칙에 대한 crud
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        minsu11       최초 생성
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReturnNameRuleService {
    private final ReturnNameRuleRepository returnNameRuleRepository;
    private final ReturnNameRuleMapper returnNameRuleMapper;

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : 요청된 name 값과 동일한 반품 정책명 규칙 응답 dto 반환 <br>
     *
     * @param name 요청된 name
     * @return return name rule response
     * @throws ReturnNameRuleNotFoundException name 값과 동일한 데이터를 찾지 못한 경우
     */
    @Transactional(readOnly = true)
    public ReturnNameRuleResponse getReturnNameRule(String name) {
        ReturnNameRule returnNameRule = returnNameRuleRepository.findByName(name).orElseThrow(ReturnNameRuleNotFoundException::new);
        return returnNameRuleMapper.mapToReturnNameRuleResponse(returnNameRule);
    }


}
