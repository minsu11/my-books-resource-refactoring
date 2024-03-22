package store.mybooks.resource.book.repotisory;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.book.dto.response.*;

/**
 * packageName    : store.mybooks.resource.book.repotisory <br/>
 * fileName       : BookRepositoryCustom<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
@NoRepositoryBean
public interface BookRepositoryCustom {
    BookDetailResponse getBookDetailInfo(Long id);

    Page<BookBriefResponse> getBookBriefInfo(Pageable pageable);

    Page<BookBriefResponse> getActiveBookBriefInfo(Pageable pageable);

    List<BookGetResponseForCoupon> getBookForCoupon();

    BookResponseForOrder getBookForOrder(Long bookId);

    void updateBookViewCount(Long bookId, Integer count);

    BookStockResponse getBookStockList(Long id);
}
