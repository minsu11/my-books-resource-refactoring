package store.mybooks.resource.bookauthor.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.repository.AuthorRepository;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookauthor.entity.BookAuthor;

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

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;


    private Long bookId;

    @BeforeEach
    public void setUp() {
        Book book = bookRepository.save(new Book());
        Author author1 = authorRepository.save(new Author());
        Author author2 = authorRepository.save(new Author());

        bookId = book.getId();

        bookAuthorRepository.save(new BookAuthor(new BookAuthor.Pk(bookId, author1.getId()), book, author1));
        bookAuthorRepository.save(new BookAuthor(new BookAuthor.Pk(bookId, author1.getId()), book, author2));
    }

    @Test
    @DisplayName("도서 ID가 있는 경우")
    void givenBookId_whenExistsByPkBookId_thenReturnTrue() {
        Assertions.assertTrue(bookAuthorRepository.existsByPk_BookId(bookId));
    }

    @Test
    @DisplayName("도서 ID가 없는 경우")
    void givenNotExistsBookId_whenExistsByPkBookId_thenReturnFalse() {
        Assertions.assertFalse(bookAuthorRepository.existsByPk_BookId(100L));
    }


    @Test
    void deleteByPk_BookId() {
        bookAuthorRepository.deleteByPk_BookId(bookId);
        Assertions.assertFalse(bookAuthorRepository.existsByPk_BookId(bookId));
    }
}