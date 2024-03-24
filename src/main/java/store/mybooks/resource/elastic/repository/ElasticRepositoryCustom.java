package store.mybooks.resource.elastic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.book.dto.response.BookBriefResponse;

/**
 * packageName    : store.mybooks.resource.elastic.repository <br/>
 * fileName       : ElasticRepositoryCustom<br/>
 * author         : newjaehun <br/>
 * date           : 3/19/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/19/24        newjaehun       최초 생성<br/>
 */
@NoRepositoryBean
public interface ElasticRepositoryCustom {
    Page<BookBriefResponse> search(String query, Pageable pageable);
}
