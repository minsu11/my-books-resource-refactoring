package store.mybooks.resource.return_rule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.exception.ReturnRuleNotExistException;
import store.mybooks.resource.return_rule.repository.ReturnRuleRepository;

/**
 * packageName    : store.mybooks.resource.return_rule.service<br>
 * fileName       : ReturnRuleService<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class ReturnRuleService {
    private final ReturnRuleRepository returnRuleRepository;

    /**
     * Gets return rule response by return rule name.
     *
     * @param returnRuleName the return rule name
     * @return the return rule response by return rule name
     */
    public ReturnRuleResponse getReturnRuleResponseByReturnRuleName(String returnRuleName) {
        return returnRuleRepository.findByReturnRuleName(returnRuleName).orElseThrow(() -> new ReturnRuleNotExistException("반품 규정이 존재하지 않음"));
    }

    public List<ReturnRuleResponse> getReturnRuleResponseList() {
        return returnRuleRepository.getReturnRuleResponseList();
    }

}
