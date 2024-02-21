package store.mybooks.resource.return_rule.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.entity.QReturnRule;
import store.mybooks.resource.return_rule_name.entity.QReturnRuleName;

/**
 * packageName    : store.mybooks.resource.return_rule.repository<br>
 * fileName       : ReturnRuleRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public class ReturnRuleRepositoryImpl extends QuerydslRepositorySupport implements ReturnRuleRepositoryCustom {
    public ReturnRuleRepositoryImpl() {
        super(ReturnRuleResponse.class);
    }

    @Override
    public Optional<ReturnRuleResponse> findByReturnRuleName(String returnRuleName) {
        QReturnRule returnRule = QReturnRule.returnRule;
        QReturnRuleName qReturnRuleName = QReturnRuleName.returnRuleName;
        return Optional.of(
                from(returnRule)
                        .select(Projections.constructor(ReturnRuleResponse.class, returnRule.deliveryFee,
                                returnRule.term, returnRule.isAvailable, returnRule.returnRuleName))
                        .join(returnRule.returnRuleName, qReturnRuleName)
                        .where(returnRule.returnRuleName.id.eq(returnRuleName))
                        .fetchOne());
    }

    @Override
    public List<ReturnRuleResponse> getReturnRuleResponseList() {
        QReturnRule returnRule = QReturnRule.returnRule;
        return from(returnRule)
                .select(Projections.constructor(ReturnRuleResponse.class, returnRule.deliveryFee,
                        returnRule.term, returnRule.isAvailable,
                        returnRule.returnRuleName))
                .fetch();
    }
}

