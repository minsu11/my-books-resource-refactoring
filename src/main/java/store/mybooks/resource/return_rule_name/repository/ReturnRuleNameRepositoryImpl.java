package store.mybooks.resource.return_rule_name.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.return_rule_name.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.return_rule_name.entity.QReturnRuleName;

/**
 * packageName    : store.mybooks.resource.return_rule_name.repository<br>
 * fileName       : ReturnRuleNameRepositoryCustomImpl<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */

public class ReturnRuleNameRepositoryImpl extends QuerydslRepositorySupport implements ReturnRuleNameRepositoryCustom {
    public ReturnRuleNameRepositoryImpl() {
        super(ReturnRuleNameResponse.class);
    }

    @Override
    public List<ReturnRuleNameResponse> getReturnRuleNameList() {
        QReturnRuleName qReturnRuleName = QReturnRuleName.returnRuleName;

        return from(qReturnRuleName)
                .select(Projections.constructor(ReturnRuleNameResponse.class, qReturnRuleName.id, qReturnRuleName.createdDate))
                .fetch();
    }
}
