package store.mybooks.resource.booklike.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.book.dto.response.BookBriefResponse;

/**
 * packageName    : store.mybooks.resource.book_like.repository <br/>
 * fileName       : BookLikeRepositoryCustom<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
@NoRepositoryBean
public interface BookLikeRepositoryCustom {
    Page<BookBriefResponse> getUserBookLike(Long userId, Pageable pageable);
}
