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
 * description    : ReturnRuleRepositoryCustom 메서드 정의.
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public class ReturnRuleRepositoryImpl extends QuerydslRepositorySupport implements ReturnRuleRepositoryCustom {
    public ReturnRuleRepositoryImpl() {
        super(ReturnRuleResponse.class);
    }

    /**
     * methodName : findByReturnRuleName<br>
     * author : minsu11<br>
     * description : {@code returnRuleNameId}에 대한 반품 규정을
     * {@code ReturnRuleResponse}으로 반환
     * <br> *
     *
     * @param returnRuleNameId 조회할 반품 규정의 이름 {@code id}
     * @return optional
     */
    @Override
    public Optional<ReturnRuleResponse> findByReturnRuleName(String returnRuleNameId) {
        QReturnRule returnRule = QReturnRule.returnRule;
        QReturnRuleName returnRuleName = QReturnRuleName.returnRuleName;
        return Optional.of(
                from(returnRule)
                        .select(Projections.constructor(ReturnRuleResponse.class, returnRule.returnRuleName.id,
                                returnRule.deliveryFee,
                                returnRule.term, returnRule.isAvailable))
                        .join(returnRule.returnRuleName, returnRuleName)
                        .where(returnRule.returnRuleName.id.eq(returnRuleNameId))
                        .fetchOne());
    }

    /**
     * methodName : getReturnRuleResponseList<br>
     * author : minsu11<br>
     * description : 모든 반품 규정의 조회해서 {@code ReturnRuleResponse} 리스트로 반환
     * <br> *
     *
     * @return optional
     */
    @Override
    public List<ReturnRuleResponse> getReturnRuleResponseList() {
        QReturnRule returnRule = QReturnRule.returnRule;
        return from(returnRule)
                .select(Projections.constructor(ReturnRuleResponse.class,
                        returnRule.returnRuleName.id,
                        returnRule.deliveryFee,
                        returnRule.term, returnRule.isAvailable
                ))
                .fetch();
    }
}

