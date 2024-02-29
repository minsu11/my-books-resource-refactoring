package store.mybooks.resource.book.repotisory;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.entity.QBook;

/**
 * packageName    : store.mybooks.resource.book.repotisory <br/>
 * fileName       : BookRepositoryImpl<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
public class BookRepositoryImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {
    public BookRepositoryImpl() {
        super(Book.class);
    }

    QBook book = QBook.book;

    @Override
    public BookDetailResponse getBookDetailInfo(Long id) {
        return null;
    }

    @Override
    public Page<BookBriefResponse> getBookBriefInfo(Pageable pageable) {
        List<BookBriefResponse> lists = getQuerydsl().applyPagination(pageable,
                        from(book)
                                .select(Projections.constructor(BookBriefResponse.class, book.id, book.name, book.saleCost)))
                .fetch();

        long total = from(book).fetchCount();

        return new PageImpl<>(lists, pageable, total);
    }
}
