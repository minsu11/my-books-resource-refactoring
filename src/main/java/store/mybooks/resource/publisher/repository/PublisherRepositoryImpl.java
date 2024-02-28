package store.mybooks.resource.publisher.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.entity.QPublisher;

/**
 * packageName    : store.mybooks.resource.publisher.repository <br/>
 * fileName       : PublisherRepositoryImpl<br/>
 * author         : newjaehun <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        newjaehun       최초 생성<br/>
 */
public class PublisherRepositoryImpl extends QuerydslRepositorySupport implements PublisherRepositoryCustom {
    public PublisherRepositoryImpl() {
        super(Publisher.class);
    }
    
    QPublisher publisher = QPublisher.publisher;

    @Override
    public List<PublisherGetResponse> findAllBy() {
        return from(publisher)
                .select(Projections.constructor(PublisherGetResponse.class, publisher.id, publisher.name))
                .fetch();

    }

    @Override
    public Page<PublisherGetResponse> getPagedBy(Pageable pageable) {
        List<PublisherGetResponse> lists = getQuerydsl().applyPagination(pageable,
                        from(publisher)
                                .select(Projections.constructor(PublisherGetResponse.class, publisher.id, publisher.name)))
                .fetch();
        long total = from(publisher).fetchCount();
        return new PageImpl<>(lists, pageable, total);
    }
}
