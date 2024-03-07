package store.mybooks.resource.point_rule_name.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.point_rule_name.dto.response.PointRuleNameResponse;
import store.mybooks.resource.point_rule_name.entity.QPointRuleName;

/**
 * packageName    : store.mybooks.resource.point_rule_name.repository<br>
 * fileName       : PointRuleNameRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
public class PointRuleNameRepositoryImpl extends QuerydslRepositorySupport implements PointRuleNameRepositoryCustom {
    public PointRuleNameRepositoryImpl() {
        super(PointRuleNameResponse.class);
    }

    private static final QPointRuleName pointRuleName = QPointRuleName.pointRuleName;

    @Override
    public Optional<PointRuleNameResponse> getPointRuleNameById(String id) {

        return Optional.of(
                from(pointRuleName)
                        .select(Projections.constructor(PointRuleNameResponse.class, pointRuleName.id))
                        .fetchOne());
    }

    @Override
    public List<PointRuleNameResponse> getPointRuleNameList() {
        return from(pointRuleName)
                .select(Projections.constructor(PointRuleNameResponse.class, pointRuleName.id))
                .fetch();
    }
}
