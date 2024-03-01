//package store.mybooks.resource.book_author.repository;
//
//import java.time.LocalDate;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import store.mybooks.resource.author.entity.Author;
//import store.mybooks.resource.author.repository.AuthorRepository;
//import store.mybooks.resource.book.entity.Book;
//import store.mybooks.resource.book.repotisory.BookRepository;
//import store.mybooks.resource.book_author.entity.BookAuthor;
//import store.mybooks.resource.book_status.entity.BookStatus;
//import store.mybooks.resource.book_status.respository.BookStatusRepository;
//import store.mybooks.resource.publisher.entity.Publisher;
//import store.mybooks.resource.publisher.repository.PublisherRepository;
//
///**
// * packageName    : store.mybooks.resource.book_author.repository <br/>
// * fileName       : BookAuthorRepositoryTest<br/>
// * author         : newjaehun <br/>
// * date           : 3/1/24<br/>
// * description    :<br/>
// * ===========================================================<br/>
// * DATE              AUTHOR             NOTE<br/>
// * -----------------------------------------------------------<br/>
// * 3/1/24        newjaehun       최초 생성<br/>
// */
//@DataJpaTest
//class BookAuthorRepositoryTest {
//    @Autowired
//    private BookAuthorRepository bookAuthorRepository;
//
//    @Autowired
//    private static BookRepository bookRepository;
//
//    @Autowired
//    private static AuthorRepository authorRepository;
//
//    @Autowired
//    private static BookStatusRepository bookStatusRepository;
//
//    @Autowired
//    private static PublisherRepository publisherRepository;
//
//    private static final Publisher publisher = new Publisher("출판사1");
//
//    private static final BookStatus bookStatus = new BookStatus("판매중");
//    private Long bookId1;
//    private static final Book book1 =
//            new Book(bookStatus, publisher, "도서1",
//                    "1234567898764",
//                    LocalDate.of(2024, 1, 1), 100, "인덱스1",
//                    "내용1", 20000, 16000, 20, 5, true);
//
//    private static final Author author1 = new Author("저자명", "저자소개");
//
//    @BeforeAll
//    static void setUpAll() {
//        bookStatusRepository.save(bookStatus);
//        publisherRepository.save(publisher);
//        authorRepository.save(author1);
//        bookRepository.save(book1);
//    }
//
//    @BeforeEach
//    public void setUp() {
//    }
//
//    @AfterEach
//    public void setOut() {
//    }
//
//    @Test
//    @DisplayName("도서 ID가 존재할 경우")
//    void givenBookId_whenExistsByPkBookId_thenReturnTrue() {
//        BookAuthor bookAuthor = new BookAuthor(new BookAuthor.Pk(bookId1, authorId1), book1, author1);
//        bookAuthorRepository.save(bookAuthor);
//        Assertions.assertTrue(bookAuthorRepository.existsByPk_BookId(bookId1));
//    }
//
//    @Test
//    @DisplayName("도서 ID가 없는 경우")
//    void givenBookId_whenExistsByPkBookId_thenReturnFalse() {
//        Assertions.assertFalse(bookAuthorRepository.existsByPk_BookId(2L));
//    }
//
//    @Test
//    void deleteByPk_BookId() {
//
//    }
//}