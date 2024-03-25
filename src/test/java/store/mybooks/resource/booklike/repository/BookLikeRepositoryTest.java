package store.mybooks.resource.booklike.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.booklike.entity.BookLike;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.book_like.repository <br/>
 * fileName       : BookLikeRepositoryTest<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
@DataJpaTest
class BookLikeRepositoryTest {
    @Autowired
    private BookLikeRepository bookLikeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("도서 좋아요 갯수 조회")
    void givenBookId_whenCountBookLikeByPk_BookId_thenReturnCount() {
        Book book = entityManager.persistFlushFind(new Book());
        User user = entityManager.persistFlushFind(new User());
        Long bookId = book.getId();
        Long userId = user.getId();

        bookLikeRepository.save(new BookLike(new BookLike.Pk(userId, bookId), user, book));
        Assertions.assertEquals(1, bookLikeRepository.countBookLikeByPk_BookId(bookId));
    }
    
}