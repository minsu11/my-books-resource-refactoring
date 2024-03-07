package store.mybooks.resource.point_rule.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.point_rule.dto.response.PointRuleResponse;

/**
 * packageName    : store.mybooks.resource.point_rule.repository<br>
 * fileName       : PointRuleRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
public class PointRuleRepositoryImpl extends QuerydslRepositorySupport implements PointRuleRepositoryCustom {

    public PointRuleRepositoryImpl() {
        super(PointRuleResponse.class);
    }


    @Override
    public PointRuleResponse getPointRuleById(Integer id) {
        return null;
    }
}
