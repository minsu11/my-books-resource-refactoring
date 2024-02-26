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

    /**
     * methodName : getReturnRuleNameList<br>
     * author : minsu11<br>
     * description : 모든 반품 규정 명을 조회해서 {@code ReturnRuleNameResponse}로 반환
     * <br> *
     *
     * @return {@code ReturnRuleNameResponse List}로 반환
     */
    @Override
    public List<ReturnRuleNameResponse> getReturnRuleNameList() {
        QReturnRuleName returnRuleName = QReturnRuleName.returnRuleName;

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
