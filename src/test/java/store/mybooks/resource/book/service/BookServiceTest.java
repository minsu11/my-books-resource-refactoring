package store.mybooks.resource.book.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.request.BookModifyRequest;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.mapper.BookMapper;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.book_category.service.BookCategoryService;
import store.mybooks.resource.book_status.entity.BookStatus;
import store.mybooks.resource.book_status.exception.BookStatusNotExistException;
import store.mybooks.resource.book_status.respository.BookStatusRepository;
import store.mybooks.resource.book_tag.dto.request.BookTagCreateRequest;
import store.mybooks.resource.book_tag.service.BookTagService;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.publisher.repository.PublisherRepository;

/**
 * packageName    : store.mybooks.resource.book.service <br/>
 * fileName       : BookServiceTest<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    public BookCategoryService bookCategoryService;
    @Mock
    public BookTagService bookTagService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("도서 추가")
    void givenBookCreateRequest_whenCreateBook_thenSaveAuthorAndReturnAuthorCreateResponse() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        BookCreateResponse response = new BookCreateResponse();
        response.setName(request.getName());

        BookStatus bookStatus = new BookStatus("판매중");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        given(publisherRepository.findById(anyInt())).willReturn(Optional.of(publisher));

        Book book = new Book(1L, bookStatus, publisher, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1",
                "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());

        given(bookRepository.save(any(Book.class))).willReturn(book);
        when(bookMapper.createResponse(any(Book.class))).thenReturn(response);


        doNothing().when(bookCategoryService).createBookCategory(any(BookCategoryCreateRequest.class));
        doNothing().when(bookTagService).createBookTag(any(BookTagCreateRequest.class));

        bookService.createBook(request);

        Assertions.assertThat(response.getName()).isEqualTo(request.getName());

        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(존재하지 않는 도서상태)")
    void givenNotExistBookStatus_whenCreateBook_thenThrowBookStatusNotExistException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, null, null);

        given(bookStatusRepository.findById(anyString())).willReturn(Optional.empty());

        assertThrows(BookStatusNotExistException.class, () -> bookService.createBook(request));
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(0)).findById(anyInt());
        verify(bookRepository, times(0)).save(any(Book.class));
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(존재하지 않는 출판사")
    void givenNotExistPublisher_whenCreateBook_thenThrowPublisherNotExistException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, null, null);
        BookCreateResponse response = new BookCreateResponse();
        response.setName(request.getName());

        BookStatus bookStatus = new BookStatus("판매중");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        given(publisherRepository.findById(anyInt())).willReturn(Optional.empty());

        assertThrows(PublisherNotExistException.class, () -> bookService.createBook(request));

        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(0)).save(any(Book.class));
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정")
    void givenBookIdAndBookModifyRequest_whenModifyBook_thenModifyBookAndReturnBookModifyResponse() {
        BookModifyRequest request = new BookModifyRequest("판매종료", 12000, -1, false);
        Long bookId = 1L;
        Book book =
                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
                        "1234567898764",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        BookModifyResponse response = new BookModifyResponse();
        response.setName(book.getName());

        BookStatus bookStatus = new BookStatus("판매종료");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        when(bookMapper.modifyResponse(any(Book.class))).thenReturn(response);

        bookService.modifyBook(bookId, request);

        Assertions.assertThat(response.getName()).isEqualTo(book.getName());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(bookMapper, times(1)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정(존재하지 않는 도서ID)")
    void givenNotExistBookId_whenModifyBook_thenThrowBookNotExistException() {
        Long bookId = 1L;
        BookModifyRequest request = new BookModifyRequest("판매종료", 12000, -1, false);

        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        assertThrows(BookNotExistException.class, () -> bookService.modifyBook(bookId, request));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(0)).findById(anyString());
        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정(존재하지 않는 도서상태)")
    void givenNotExistBookStatus_whenModifyBook_thenThrowBookStatusNotExistException() {
        Long bookId = 1L;
        BookModifyRequest request = new BookModifyRequest("판매종료", 12000, -1, false);

        Book book =
                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
                        "1234567898764",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        given(bookStatusRepository.findById(anyString())).willReturn(Optional.empty());

        assertThrows(BookStatusNotExistException.class, () -> bookService.modifyBook(bookId, request));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
    }
}