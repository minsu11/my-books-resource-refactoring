package store.mybooks.resource.return_rule_name.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.return_rule_name.dto.mapper.ReturnRuleNameMapper;
import store.mybooks.resource.return_rule_name.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.return_rule_name.entity.ReturnRuleName;
import store.mybooks.resource.return_rule_name.exception.ReturnRuleNameNotFoundException;
import store.mybooks.resource.return_rule_name.repository.ReturnRuleNameRepository;

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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReturnRuleNameService {
    private final ReturnRuleNameRepository returnRuleNameRepository;
    private final ReturnRuleNameMapper returnRuleNameMapper;

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : 요청된 name 값과 동일한 반품 규정 명 조회 <br>
     *
     * @param name 요청된 name
     * @return return name rule response
     * @throws ReturnRuleNameNotFoundException name 값과 동일한 데이터를 찾지 못한 경우
     */
    public ReturnRuleNameResponse getReturnRuleName(String name) {
        ReturnRuleName returnNameRule = returnRuleNameRepository.findById(name)
                .orElseThrow(() -> new ReturnRuleNameNotFoundException("반품 규정 명 규칙에 대한 데이터가 없음"));
        return returnRuleNameMapper.mapToReturnRuleNameResponse(returnNameRule);
    }

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : 반품 규정 명 테이블의 모든 데이터를 조회 <br>
     *
     * @return the return rule name list
     */
    public List<ReturnRuleNameResponse> getReturnRuleNameList() {
        return returnRuleNameRepository.getReturnRuleNameList();
    }


}
