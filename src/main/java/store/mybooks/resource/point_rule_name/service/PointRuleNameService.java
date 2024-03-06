package store.mybooks.resource.point_rule_name.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.point_rule_name.dto.response.PointRuleNameResponse;
import store.mybooks.resource.point_rule_name.exception.PointRuleNameNotExistException;
import store.mybooks.resource.point_rule_name.repository.PointRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.point_rule_name.service<br>
 * fileName       : PointRuleNameService<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class PointRuleNameService {
    private final PointRuleNameRepository pointRuleNameRepository;

    public PointRuleNameResponse getPointRuleName(String id) {

        return pointRuleNameRepository.getPointRuleNameById(id).orElseThrow(PointRuleNameNotExistException::new);

    }
}
