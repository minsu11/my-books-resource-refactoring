package store.mybooks.resource.wrap.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.entity.QWrap;

/**
 * packageName    : store.mybooks.resource.wrap.repository<br>
 * fileName       : WrapRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */

public class WrapRepositoryImpl extends QuerydslRepositorySupport implements WrapRepositoryCustom {
    public WrapRepositoryImpl() {
        super(WrapResponse.class);
    }

    @Override
    public Optional<WrapResponse> findWrapResponseById(Integer id) {
        QWrap qWrap = QWrap.wrap;

        return Optional.of(from(qWrap)
                .select(Projections.constructor(WrapResponse.class,
                        qWrap.name,
                        qWrap.cost,
                        qWrap.isAvailable))
                .fetchOne());
    }

    @Override
    public List<WrapResponse> getWrapResponseList() {
        QWrap qWrap = QWrap.wrap;
        return from(qWrap)
                .select(Projections.constructor(WrapResponse.class,
                        qWrap.name,
                        qWrap.cost,
                        qWrap.isAvailable))
                .fetch();
    }

    @Override
    public Boolean existWrap(String wrapName) {
        QWrap qWrap = QWrap.wrap;
        return Objects.nonNull(
                from(qWrap)
                        .where(qWrap.name.eq(wrapName)
                                .and(qWrap.isAvailable.eq(true)))
                        .fetch()
        );
    }
}
