package store.mybooks.resource.returnrulename.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.entity.QReturnRuleName;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;

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
    private static final QReturnRuleName returnRuleName = QReturnRuleName.returnRuleName;

    public ReturnRuleNameRepositoryImpl() {
        super(ReturnRuleName.class);
    }

    @Override
    public Optional<ReturnRuleNameResponse> findReturnRuleNameById(String id) {
        return Optional.of(
                from(returnRuleName)
                        .select(Projections.constructor(
                                ReturnRuleNameResponse.class,
                                returnRuleName.id,
                                returnRuleName.createdDate
                        ))
                        .where(returnRuleName.id.eq(id))
                        .fetchOne());
    }


    @Override
    public List<ReturnRuleNameResponse> getReturnRuleNameList() {

        return from(returnRuleName)
                .select(
                        Projections.constructor(
                                ReturnRuleNameResponse.class,
                                returnRuleName.id,
                                returnRuleName.createdDate)
                )
                .fetch();
    }
}
