//package store.mybooks.resource.booklike.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.util.ReflectionTestUtils;
//import store.mybooks.resource.book.dto.response.BookBriefResponse;
//import store.mybooks.resource.book.entity.Book;
//import store.mybooks.resource.book.exception.BookNotExistException;
//import store.mybooks.resource.book.repotisory.BookRepository;
//import store.mybooks.resource.booklike.entity.BookLike;
//import store.mybooks.resource.booklike.repository.BookLikeRepository;
//import store.mybooks.resource.user.entity.User;
//import store.mybooks.resource.user.exception.UserNotExistException;
//import store.mybooks.resource.user.repository.UserRepository;
//
///**
// * packageName    : store.mybooks.resource.book_like.service <br/>
// * fileName       : BookLikeServiceTest<br/>
// * author         : newjaehun <br/>
// * date           : 3/7/24<br/>
// * description    :<br/>
// * ===========================================================<br/>
// * DATE              AUTHOR             NOTE<br/>
// * -----------------------------------------------------------<br/>
// * 3/7/24        newjaehun       최초 생성<br/>
// */
//@ExtendWith(MockitoExtension.class)
//class BookLikeServiceTest {
//    @Mock
//    BookLikeRepository bookLikeRepository;
//    @Mock
//    BookRepository bookRepository;
//    @Mock
//    UserRepository userRepository;
//
//    @InjectMocks
//    BookLikeService bookLikeService;
//
//    private final Long userId = 1L;
//
//    private final Long bookId = 2L;
//
//    @Test
//    @DisplayName("사용자가 좋아요한 페이징된 도서 목록(존재하는 회원")
//    void givenUserId_whenGetUserBookLike_thenReturnPagedBookBriefResponse() {
//        Pageable pageable = PageRequest.of(0, 2);
//        Long bookId2 = 3L;
//
//        List<BookBriefResponse> list = Arrays.asList(new BookBriefResponse(bookId, "name", 10000),
//                new BookBriefResponse(bookId2, "name", 10000));
//        Page<BookBriefResponse> response = new PageImpl<>(list, pageable, list.size());
//        given(userRepository.existsById(userId)).willReturn(true);
//        when(bookLikeRepository.getUserBookLike(userId, pageable)).thenReturn(response);
//
//        assertThat(bookLikeService.getUserBookLike(userId, pageable)).isEqualTo(response);
//
//        verify(userRepository, times(1)).existsById(userId);
//        verify(bookLikeRepository, times(1)).getUserBookLike(userId, pageable);
//    }
//
//    @Test
//    @DisplayName("사용자가 좋아요한 페이징된 도서 목록(존재하지 않는 회원")
//    void givenNotExistedUserId_whenGetUserBookLike_thenThrowUserNotExistException() {
//        given(userRepository.existsById(userId)).willReturn(false);
//        assertThrows(UserNotExistException.class, () -> bookLikeService.getUserBookLike(userId, null));
//
//        verify(userRepository, times(1)).existsById(userId);
//        verify(bookLikeRepository, times(0)).getUserBookLike(userId, null);
//    }
//
//    @Test
//    @DisplayName("사용자가 도서 좋아요 등록(존재하지 않는 회원)")
//    void givenNotExistedUserId_whenUpdateUserBookLike_thenThrowUserNotExistException() {
//        given(userRepository.findById(userId)).willReturn(Optional.empty());
//        assertThrows(UserNotExistException.class, () -> bookLikeService.updateUserBookLike(userId, bookId));
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookRepository, times(0)).findById(anyLong());
//        verify(bookLikeRepository, times(0)).existsById(any(BookLike.Pk.class));
//        verify(bookLikeRepository, times(0)).save(any(BookLike.class));
//        verify(bookLikeRepository, times(0)).deleteById(any(BookLike.Pk.class));
//    }
//
//    @Test
//    @DisplayName("사용자가 도서 좋아요 등록(존재하는 회원, 존재하지 않는 도서)")
//    void givenUserIdAndNotExistedBookId_whenUpdateUserBookLike_thenThrowBookNotExistException() {
//        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
//        given(bookRepository.findById(bookId)).willReturn(Optional.empty());
//        assertThrows(BookNotExistException.class, () -> bookLikeService.updateUserBookLike(userId, bookId));
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookRepository, times(1)).findById(anyLong());
//        verify(bookLikeRepository, times(0)).existsById(any(BookLike.Pk.class));
//        verify(bookLikeRepository, times(0)).save(any(BookLike.class));
//        verify(bookLikeRepository, times(0)).deleteById(any(BookLike.Pk.class));
//    }
//
//    @Test
//    @DisplayName("사용자가 도서 좋아요 등록(좋아요가 안 되어 있는 경우")
//    void givenUserIdAndBookIdAndNotExistedBookLike_whenUpdateUserBookLike_thenReturnTrue() {
//        User user = new User();
//        ReflectionTestUtils.setField(user, "id", userId);
//        Book book = new Book();
//        ReflectionTestUtils.setField(book, "id", bookId);
//
//        given(userRepository.findById(userId)).willReturn(Optional.of(user));
//        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
//
//        BookLike.Pk pk = new BookLike.Pk(userId, bookId);
//
//        given(bookLikeRepository.existsById(pk)).willReturn(false);
//
//        when(bookLikeRepository.save(any(BookLike.class))).thenReturn(new BookLike(pk, user, book));
//
//        assertTrue(bookLikeService.updateUserBookLike(userId, bookId));
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookRepository, times(1)).findById(anyLong());
//        verify(bookLikeRepository, times(1)).existsById(any(BookLike.Pk.class));
//        verify(bookLikeRepository, times(1)).save(any(BookLike.class));
//        verify(bookLikeRepository, times(0)).deleteById(any(BookLike.Pk.class));
//    }
//
//    @Test
//    @DisplayName("사용자가 도서 좋아요 취소(좋아요가 되어 있는 경우")
//    void givenUserIdAndBookIdAndExistedBookLike_whenUpdateUserBookLike_thenReturnFalse() {
//        User user = new User();
//        ReflectionTestUtils.setField(user, "id", userId);
//        Book book = new Book();
//        ReflectionTestUtils.setField(book, "id", bookId);
//
//        given(userRepository.findById(userId)).willReturn(Optional.of(user));
//        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
//
//        BookLike.Pk pk = new BookLike.Pk(userId, bookId);
//
//        given(bookLikeRepository.existsById(pk)).willReturn(true);
//
//        doNothing().when(bookLikeRepository).deleteById(pk);
//
//        assertFalse(bookLikeService.updateUserBookLike(userId, bookId));
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(bookRepository, times(1)).findById(anyLong());
//        verify(bookLikeRepository, times(1)).existsById(any(BookLike.Pk.class));
//        verify(bookLikeRepository, times(0)).save(any(BookLike.class));
//        verify(bookLikeRepository, times(1)).deleteById(any(BookLike.Pk.class));
//    }
//
//
//}