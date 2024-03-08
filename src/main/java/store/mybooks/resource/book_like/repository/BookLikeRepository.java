package store.mybooks.resource.book_like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.book_like.entity.BookLike;

/**
 * packageName    : store.mybooks.resource.book_like.repository <br/>
 * fileName       : BookLikeRepository<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
public interface BookLikeRepository extends JpaRepository<BookLike, BookLike.Pk>, BookLikeRepositoryCustom {
    Integer countBookLikeByPk_BookId(Long bookId);
}
