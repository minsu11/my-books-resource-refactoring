package store.mybooks.resource.return_rule.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;

/**
 * packageName    : store.mybooks.resource.return_rule.repository<br>
 * fileName       : ReturnRuleRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface ReturnRuleRepositoryCustom {
    Optional<ReturnRuleResponse> findByReturnRuleName(String returnRuleName);

    List<ReturnRuleResponse> getReturnRuleResponseList();
}
