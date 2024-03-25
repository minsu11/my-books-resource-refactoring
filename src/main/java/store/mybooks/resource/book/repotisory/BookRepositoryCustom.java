package store.mybooks.resource.book.repotisory;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookGetResponseForCoupon;
import store.mybooks.resource.book.dto.response.BookLikeResponse;
import store.mybooks.resource.book.dto.response.BookPopularityResponse;
import store.mybooks.resource.book.dto.response.BookPublicationDateResponse;
import store.mybooks.resource.book.dto.response.BookRatingResponse;
import store.mybooks.resource.book.dto.response.BookResponseForOrder;
import store.mybooks.resource.book.dto.response.BookReviewResponse;
import store.mybooks.resource.book.dto.response.BookStockResponse;

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

    List<BookGetResponseForCoupon> getBookForCoupon();

    BookResponseForOrder getBookForOrder(Long bookId);

    void updateBookViewCount(Long bookId, Integer count);

    BookStockResponse getBookStockList(Long id);

    List<BookPopularityResponse> getBookPopularity();

    List<BookLikeResponse> getBookLike();

    List<BookReviewResponse> getBookReview();

    List<BookRatingResponse> getBookRating();

    List<BookPublicationDateResponse> getBookPublicationDate();
}
