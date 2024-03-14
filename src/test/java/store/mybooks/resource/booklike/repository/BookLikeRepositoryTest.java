package store.mybooks.resource.booklike.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.booklike.entity.BookLike;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.repository.UserRepository;

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
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

//    @Test
//    @DisplayName("도서 좋아요 갯수 조회")
//    void givenBookId_whenCountBookLikeByPk_BookId_thenReturnCount() {
//        Book book = bookRepository.save(new Book());
//        User user = userRepository.save(new User());
//        Long bookId = book.getId();
//        Long userId = user.getId();
//        bookLikeRepository.save(new BookLike(new BookLike.Pk(userId, bookId), user, book));
//        Assertions.assertEquals(1, bookLikeRepository.countBookLikeByPk_BookId(bookId));
//    }

//    @Test
//    @DisplayName("사용자가 좋아요한 도서 목록 조회")
//    void givenUserIdAndPageable_whenGetUserBookLike_thenReturnPagedBookBriefResponse() {
//        Book book1 = bookRepository.save(new Book());
//        Book book2 = bookRepository.save(new Book());
//        Long bookId1 = book1.getId();
//        Long bookId2 = book2.getId();
//
//        User user = userRepository.save(new User());
//        Long userId = user.getId();
//
//        BookLike bookLike1 = new BookLike(new BookLike.Pk(userId, bookId1), user, book1);
//        BookLike bookLike2 = new BookLike(new BookLike.Pk(userId, bookId2), user, book2);
//        bookLikeRepository.save(bookLike1);
//        bookLikeRepository.save(bookLike2);
//
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<BookBriefResponse> result = bookLikeRepository.getUserBookLike(userId, pageable);
//
//        assertThat(result.getContent()).hasSize(2);
//        assertThat(result.getContent().get(0).getId()).isEqualTo(bookId1);
//        assertThat(result.getContent().get(1).getId()).isEqualTo(bookId2);
//    }
}