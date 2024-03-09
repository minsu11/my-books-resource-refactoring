package store.mybooks.resource.booklike.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.booklike.entity.QBookLike;
import store.mybooks.resource.user.entity.QUser;

/**
 * packageName    : store.mybooks.resource.book_like.repository <br/>
 * fileName       : BookLikeRepositoryImpl<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
public class BookLikeRepositoryImpl extends QuerydslRepositorySupport implements BookLikeRepositoryCustom {
    public BookLikeRepositoryImpl() {
        super(Book.class);
    }

    QBook book = QBook.book;
    QUser user = QUser.user;
    QBookLike bookLike = QBookLike.bookLike;

    @Override
    public Page<BookBriefResponse> getUserBookLike(Long userId, Pageable pageable) {
        List<BookBriefResponse> lists =
                from(bookLike)
                        .join(bookLike.user, user)
                        .join(bookLike.book, book)
                        .where(bookLike.user.id.eq(userId))
                        .select(Projections.constructor(BookBriefResponse.class,
                                book.id, book.name, book.saleCost))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = from(bookLike)
                .join(bookLike.user, user)
                .join(bookLike.book, book)
                .where(bookLike.user.id.eq(userId))
                .fetchCount();

        return new PageImpl<>(lists, pageable, total);
    }
}
