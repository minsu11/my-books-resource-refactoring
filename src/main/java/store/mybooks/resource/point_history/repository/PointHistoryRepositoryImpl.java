package store.mybooks.resource.point_history.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.point_history.dto.response.PointResponse;
import store.mybooks.resource.point_history.entity.QPointHistory;

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
public class PointHistoryRepositoryImpl extends QuerydslRepositorySupport implements PointHistoryRepositoryCustom {
    private static final QPointHistory pointHistory = QPointHistory.pointHistory;

    public PointHistoryRepositoryImpl() {
        super(PointResponse.class);
    }

    @Override
    public PointResponse getRemainingPoint(Long userId) {
        return from(pointHistory)
                .select(Projections.constructor(PointResponse.class, pointHistory.pointStatusCost.sum()))
                .where(pointHistory.user.id.eq(userId))
                .fetchOne();
    }
}
