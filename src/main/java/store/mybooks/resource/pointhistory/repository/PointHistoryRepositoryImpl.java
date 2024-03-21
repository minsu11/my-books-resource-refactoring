package store.mybooks.resource.pointhistory.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.bookorder.entity.QBookOrder;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.entity.PointHistory;
import store.mybooks.resource.pointhistory.entity.QPointHistory;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;
import store.mybooks.resource.pointrule.entity.QPointRule;

/**
 * packageName    : store.mybooks.resource.point_history.repository<br>
 * fileName       : PointHistoryRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Slf4j
public class PointHistoryRepositoryImpl extends QuerydslRepositorySupport implements PointHistoryRepositoryCustom {
    private static final QPointHistory pointHistory = QPointHistory.pointHistory;

    public PointHistoryRepositoryImpl() {
        super(PointHistory.class);
    }

    @Override
    public PointResponse getRemainingPoint(Long userId) {
        int earnPoint = from(pointHistory)
                .select(pointHistory.pointStatusCost.sum())
                .where(pointHistory.user.id.eq(userId))
                .where(pointHistory.pointRule.pointRuleName.id
                        .notEqualsIgnoreCase(PointRuleNameEnum.USE_POINT.getValue()))
                .fetchOne();

        Integer usedPoint = Optional.ofNullable(from(pointHistory)
                .select(pointHistory.pointStatusCost.sum())
                .where(pointHistory.user.id.eq(userId))
                .where(pointHistory.pointRule.pointRuleName.id
                        .eq(PointRuleNameEnum.USE_POINT.getValue()))
                .fetchOne()).orElse(0);

        return new PointResponse(earnPoint - usedPoint);
    }

    @Override
    public Page<PointHistoryResponse> getPointHistoryByUserId(Pageable pageable, Long userId) {
        QBookOrder bookOrder = QBookOrder.bookOrder;

        List<PointHistoryResponse> pointHistoryResponses =
                from(pointHistory)
                        .where(pointHistory.user.id.eq(userId))
                        .orderBy(pointHistory.createdDate.desc())
                        .select(Projections.constructor(PointHistoryResponse.class,
                                bookOrder.number,
                                pointHistory.pointRule.pointRuleName.id,
                                pointHistory.pointStatusCost,
                                pointHistory.createdDate))
                        .leftJoin(pointHistory.bookOrder, bookOrder)
                        .where(pointHistory.user.id.eq(userId))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = from(pointHistory)
                .where(pointHistory.user.id.eq(userId))
                .fetchCount();

        return new PageImpl<>(pointHistoryResponses, pageable, total);
    }

    @Override
    public boolean isAlreadyReceivedSignUpPoint(Long userId) {
        QPointRule pointRule = QPointRule.pointRule;

        List<Long> pointHistoryIdList = from(pointHistory)
                .leftJoin(pointRule)
                .on(pointHistory.pointRule.id.eq(pointRule.id))
                .where(pointHistory.user.id.eq(userId))
                .where(pointRule.pointRuleName.id.eq("회원가입 적립"))
                .select(pointHistory.id)
                .fetch();

        return !pointHistoryIdList.isEmpty();
    }
}
