package store.mybooks.resource.book_author.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book_author.entity.BookAuthor;

/**
 * packageName    : store.mybooks.resource.book_author.repository <br/>
 * fileName       : BookAuthorRepositoryTest<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@DataJpaTest
class BookAuthorRepositoryTest {
    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    private BookAuthor bookAuthor1;
    private BookAuthor bookAuthor2;

    private final Long bookId1 = 1L;

    @BeforeEach
    public void setUp() {
        bookAuthor1 = new BookAuthor(new BookAuthor.Pk(bookId1, 1), new Book(), new Author());
        bookAuthor2 = new BookAuthor(new BookAuthor.Pk(2L, 1), new Book(), new Author());
        bookAuthorRepository.save(bookAuthor1);
        bookAuthorRepository.save(bookAuthor2);
    }

    @Test
    @DisplayName("도서 ID가 있는 경우")
    void givenBookId_whenExistsByPkBookId_thenReturnTrue() {
        Assertions.assertTrue(bookAuthorRepository.existsByPk_BookId(bookId1));
    }

    @Test
    @DisplayName("도서 ID가 없는 경우")
    void givenNotExistsBookId_whenExistsByPkBookId_thenReturnFalse() {
        Assertions.assertFalse(bookAuthorRepository.existsByPk_BookId(3L));
    }


    @Test
    void deleteByPk_BookId() {
        bookAuthorRepository.deleteByPk_BookId(bookId1);
        Assertions.assertFalse(bookAuthorRepository.existsByPk_BookId(bookId1));
    }
}