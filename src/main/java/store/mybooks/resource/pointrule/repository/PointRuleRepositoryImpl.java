package store.mybooks.resource.pointrule.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.pointrule.dto.response.PointRuleResponse;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.entity.QPointRule;

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

    private static final QPointRule pointRule = QPointRule.pointRule;

    public PointRuleRepositoryImpl() {
        super(PointRuleResponse.class);
    }


    @Override
    public Optional<PointRuleResponse> getPointRuleById(Integer id) {
        return Optional.of(from(pointRule)
                .select(Projections.constructor(
                        PointRuleResponse.class,
                        pointRule.id,
                        pointRule.pointRuleName.id,
                        pointRule.rate,
                        pointRule.cost))
                .where(pointRule.isAvailable.eq(true)
                        .and(pointRule.id.eq(id)))
                .fetchOne());
    }

    @Override
    public List<PointRuleResponse> getPointRuleList() {
        return from(pointRule)
                .select(Projections.constructor(PointRuleResponse.class,
                        pointRule.id,
                        pointRule.pointRuleName.id,
                        pointRule.rate,
                        pointRule.cost))
                .where(pointRule.isAvailable.eq(true))
                .fetch();
    }

    @Override
    public PointRule findPointRuleByPointRuleName(String pointRuleName) {
        return
                from(pointRule)
                        .where(pointRule.isAvailable.eq(true)
                                .and(
                                        pointRule.pointRuleName.id.eq(pointRuleName)))
                        .fetchOne();
    }

    @Override
    public Page<PointRuleResponse> getPointRuleResponsePage(Pageable pageable) {
        Querydsl querydsl = getQuerydsl();
        if (Objects.isNull(querydsl)) {
            throw new NullPointerException("query dsl null");
        }
        List<PointRuleResponse> pointRuleResponseList = querydsl.applyPagination(pageable,
                from(pointRule)
                        .select(Projections.constructor(PointRuleResponse.class,
                                pointRule.id,
                                pointRule.pointRuleName.id,
                                pointRule.rate,
                                pointRule.cost))
                        .where(pointRule.isAvailable.eq(true))).fetch();
        long total = from(pointRule).fetchCount();
        return new PageImpl<>(pointRuleResponseList, pageable, total);
    }
}
