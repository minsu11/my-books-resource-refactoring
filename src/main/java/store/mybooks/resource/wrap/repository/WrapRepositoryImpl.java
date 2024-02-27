package store.mybooks.resource.wrap.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.wrap.dto.response.WrapPageResponse;
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

    private final QWrap wrap = QWrap.wrap;

    @Override
    public Optional<WrapResponse> findWrapResponseById(Integer id) {

        return Optional.of(from(wrap)
                .select(Projections.constructor(WrapResponse.class,
                        wrap.name,
                        wrap.cost,
                        wrap.isAvailable))
                .where(wrap.id.eq(id)
                        .and(wrap.isAvailable.eq(true)))
                .fetchOne());
    }

    @Override
    public List<WrapResponse> getWrapResponseList() {
        return from(wrap)
                .select(Projections.constructor(WrapResponse.class,
                        wrap.name,
                        wrap.cost,
                        wrap.isAvailable))
                .fetch();
    }

    @Override
    public Boolean existWrap(String wrapName) {
        return Objects.nonNull(
                from(wrap)
                        .where(wrap.name.eq(wrapName)
                                .and(wrap.isAvailable.eq(true)))
                        .fetch()
        );
    }

    @Override
    public Page<WrapPageResponse> getPageBy(Pageable pageable) {
        List<WrapPageResponse> wrapResponses = getQuerydsl().applyPagination(pageable,
                from(wrap).select(
                        Projections.constructor(WrapPageResponse.class, wrap.id,
                                wrap.name, wrap.cost, wrap.isAvailable)
                ).where(wrap.isAvailable.eq(true))).fetch();
        long total = from().fetchCount();

        return new PageImpl<>(wrapResponses, pageable, total);
    }
}
