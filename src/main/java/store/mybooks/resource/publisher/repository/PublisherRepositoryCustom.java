package store.mybooks.resource.publisher.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;

/**
 * packageName    : store.mybooks.resource.publisher.repository <br/>
 * fileName       : PublisherRepositoryCustom<br/>
 * author         : newjaehun <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        newjaehun       최초 생성<br/>
 */
@NoRepositoryBean
public interface PublisherRepositoryCustom {
    List<PublisherGetResponse> findAllBy();

    Page<PublisherGetResponse> getPagedBy(Pageable pageable);

}
