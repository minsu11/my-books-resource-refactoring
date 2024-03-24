package store.mybooks.resource.book.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.IsbnAlreadyExistsException;
import store.mybooks.resource.book.mapper.BookMapper;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookauthor.dto.request.BookAuthorCreateRequest;
import store.mybooks.resource.bookauthor.service.BookAuthorService;
import store.mybooks.resource.bookcategory.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.bookcategory.service.BookCategoryService;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.bookstatus.exception.BookStatusNotExistException;
import store.mybooks.resource.bookstatus.respository.BookStatusRepository;
import store.mybooks.resource.booktag.dto.request.BookTagCreateRequest;
import store.mybooks.resource.booktag.service.BookTagService;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.service.ImageService;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.image_status.exception.ImageStatusNotExistException;
import store.mybooks.resource.image_status.repository.ImageStatusRepository;
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
    public ImageStatusRepository imageStatusRepository;

    @Mock
    public BookAuthorService bookAuthorService;

    @Mock
    public BookTagService bookTagService;

    @Mock
    public ImageService imageService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("도서 추가")
    void givenBookCreateRequest_whenCreateBook_thenSaveAuthorAndReturnAuthorCreateResponse() throws IOException {
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

        given(bookRepository.existsByIsbn(anyString())).willReturn(false);

        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        List<MultipartFile> contents =
                List.of(new MockMultipartFile("content", "content.png", "image/png", "content".getBytes()),
                        new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes()));

        ImageStatus thumbnailStatus = new ImageStatus(ImageStatusEnum.THUMBNAIL.getName());
        ImageStatus contentStatus = new ImageStatus(ImageStatusEnum.CONTENT.getName());

        when(imageStatusRepository.findById(ImageStatusEnum.THUMBNAIL.getName())).thenReturn(
                Optional.of(thumbnailStatus));
        when(imageStatusRepository.findById(ImageStatusEnum.CONTENT.getName())).thenReturn(Optional.of(contentStatus));


        given(bookRepository.save(any(Book.class))).willReturn(book);
        when(bookMapper.createResponse(any(Book.class))).thenReturn(response);

        doNothing().when(bookAuthorService).createBookAuthor(any(BookAuthorCreateRequest.class));
        doNothing().when(bookCategoryService).createBookCategory(any(BookCategoryCreateRequest.class));
        doNothing().when(bookTagService).createBookTag(any(BookTagCreateRequest.class));

        when(imageService
                .saveImage(any(ImageStatus.class), eq(null), any(Book.class), any(MultipartFile.class))).thenReturn(
                new ImageRegisterResponse());

        bookService.createBook(request, thumbnailFile, contents);

        Assertions.assertThat(response.getName()).isEqualTo(request.getName());

        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(imageStatusRepository, times(2)).findById(anyString());
        verify(bookMapper, times(1)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(존재하지 않는 도서상태)")
    void givenNotExistBookStatus_whenCreateBook_thenThrowBookStatusNotExistException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        List<MultipartFile> contents =
                List.of(new MockMultipartFile("content", "content.png", "image/png", "content".getBytes()),
                        new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes()));

        given(bookStatusRepository.findById(anyString())).willReturn(Optional.empty());

        assertThrows(BookStatusNotExistException.class, () -> bookService.createBook(request, thumbnailFile, contents));

        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(0)).findById(anyInt());
        verify(bookRepository, times(0)).existsByIsbn(anyString());
        verify(bookRepository, times(0)).save(any(Book.class));
        verify(imageStatusRepository, times(0)).findById(anyString());
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(존재하지 않는 출판사)")
    void givenNotExistPublisher_whenCreateBook_thenThrowPublisherNotExistException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, null, null);
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        List<MultipartFile> contents =
                List.of(new MockMultipartFile("content", "content.png", "image/png", "content".getBytes()),
                        new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes()));

        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(new BookStatus("판매중")));

        given(publisherRepository.findById(anyInt())).willReturn(Optional.empty());

        assertThrows(PublisherNotExistException.class, () -> bookService.createBook(request, thumbnailFile, contents));
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(0)).existsByIsbn(anyString());
        verify(bookRepository, times(0)).save(any(Book.class));
        verify(imageStatusRepository, times(0)).findById(anyString());
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(이미 존재하는 ISBN)")
    void givenAlreadyExistsIsbn_whenCreateBook_thenThrowIsbnAlreadyExistsException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, null, null);
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        List<MultipartFile> contents =
                List.of(new MockMultipartFile("content", "content.png", "image/png", "content".getBytes()),
                        new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes()));

        BookStatus bookStatus = new BookStatus("판매중");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        given(publisherRepository.findById(anyInt())).willReturn(Optional.of(publisher));

        given(bookRepository.existsByIsbn(anyString())).willReturn(true);

        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.createBook(request, thumbnailFile, contents));
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookRepository, times(0)).save(any(Book.class));
        verify(imageStatusRepository, times(0)).findById(anyString());
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(존재하지 않는 이미지 상태-썸네일)")
    void givenNotExistsThumbnailImageStatus_whenCreateBook_thenThrowImageStatusNotExistException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, null, null);
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        List<MultipartFile> contents =
                List.of(new MockMultipartFile("content", "content.png", "image/png", "content".getBytes()),
                        new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes()));

        BookStatus bookStatus = new BookStatus("판매중");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        given(publisherRepository.findById(anyInt())).willReturn(Optional.of(publisher));

        given(bookRepository.existsByIsbn(anyString())).willReturn(false);

        Book book = new Book(1L, bookStatus, publisher, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1",
                "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.save(any(Book.class))).willReturn(book);

        when(imageStatusRepository.findById(ImageStatusEnum.THUMBNAIL.getName())).thenReturn(
                Optional.empty());

        assertThrows(ImageStatusNotExistException.class,
                () -> bookService.createBook(request, thumbnailFile, contents));
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(imageStatusRepository, times(1)).findById(anyString());
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 추가(존재하지 않는 이미지 상태-본문)")
    void givenNotExistsContentImageStatus_whenCreateBook_thenThrowImageStatusNotExistException() {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, null, null);
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        List<MultipartFile> contents =
                List.of(new MockMultipartFile("content", "content.png", "image/png", "content".getBytes()),
                        new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes()));

        BookStatus bookStatus = new BookStatus("판매중");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        given(publisherRepository.findById(anyInt())).willReturn(Optional.of(publisher));

        given(bookRepository.existsByIsbn(anyString())).willReturn(false);

        Book book = new Book(1L, bookStatus, publisher, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1",
                "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.save(any(Book.class))).willReturn(book);

        ImageStatus thumbnailStatus = new ImageStatus(ImageStatusEnum.THUMBNAIL.getName());

        when(imageStatusRepository.findById(ImageStatusEnum.THUMBNAIL.getName())).thenReturn(
                Optional.of(thumbnailStatus));
        when(imageStatusRepository.findById(ImageStatusEnum.CONTENT.getName())).thenReturn(
                Optional.empty());

        assertThrows(ImageStatusNotExistException.class,
                () -> bookService.createBook(request, thumbnailFile, contents));
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(imageStatusRepository, times(2)).findById(anyString());
        verify(bookMapper, times(0)).createResponse(any(Book.class));
    }
//    @Test
//    @DisplayName("도서 수정")
//    void givenBookIdAndBookModifyRequest_whenModifyBook_thenModifyBookAndReturnBookModifyResponse() {
//        BookModifyRequest request = new BookModifyRequest();
//        ReflectionTestUtils.setField(request, "saleCost", 1);
//        ReflectionTestUtils.setField(request, "bookStatusId", "판매종료");
//        ReflectionTestUtils.setField(request, "stock", 0);
//        ReflectionTestUtils.setField(request, "isPacking", true);
//        ReflectionTestUtils.setField(request, "categoryList", new ArrayList<>(List.of(1)));
//        ReflectionTestUtils.setField(request, "tagList", null);
//        Long bookId = 1L;
//        Book book =
//                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
//                        "1234567898764",
//                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
//                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
//        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
//
//        BookModifyResponse response = new BookModifyResponse();
//        response.setName(book.getName());
//
//        BookStatus bookStatus = new BookStatus("판매종료");
//        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));
//
//        when(bookMapper.modifyResponse(any(Book.class))).thenReturn(response);
//
//        bookService.modifyBook(bookId, request);
//
//        Assertions.assertThat(response.getName()).isEqualTo(book.getName());
//
//        verify(bookRepository, times(1)).findById(bookId);
//        verify(bookStatusRepository, times(1)).findById(anyString());
//        verify(bookMapper, times(1)).modifyResponse(any(Book.class));
//    }
//
//    @Test
//    @DisplayName("도서 수정(존재하지 않는 도서ID)")
//    void givenNotExistBookId_whenModifyBook_thenThrowBookNotExistException() {
//        Long bookId = 1L;
//        BookModifyRequest request = new BookModifyRequest();
//        ReflectionTestUtils.setField(request, "saleCost", 1);
//        ReflectionTestUtils.setField(request, "bookStatusId", "판매종료");
//        ReflectionTestUtils.setField(request, "stock", 0);
//        ReflectionTestUtils.setField(request, "isPacking", true);
//        ReflectionTestUtils.setField(request, "categoryList", new ArrayList<>(List.of(1)));
//        ReflectionTestUtils.setField(request, "tagList", null);
//
//        given(bookRepository.findById(bookId)).willReturn(Optional.empty());
//
//        assertThrows(BookNotExistException.class, () -> bookService.modifyBook(bookId, request));
//
//        verify(bookRepository, times(1)).findById(bookId);
//        verify(bookStatusRepository, times(0)).findById(anyString());
//        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
//    }
//
//    @Test
//    @DisplayName("도서 수정(존재하지 않는 도서상태)")
//    void givenNotExistBookStatus_whenModifyBook_thenThrowBookStatusNotExistException() {
//        Long bookId = 1L;
//        BookModifyRequest request = new BookModifyRequest();
//        ReflectionTestUtils.setField(request, "saleCost", 1);
//        ReflectionTestUtils.setField(request, "bookStatusId", "판매종료");
//        ReflectionTestUtils.setField(request, "stock", 0);
//        ReflectionTestUtils.setField(request, "isPacking", true);
//        ReflectionTestUtils.setField(request, "categoryList", new ArrayList<>(List.of(1)));
//        ReflectionTestUtils.setField(request, "tagList", null);
//
//        Book book =
//                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
//                        "1234567898764",
//                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
//                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
//        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
//
//        given(bookStatusRepository.findById(anyString())).willReturn(Optional.empty());
//
//        assertThrows(BookStatusNotExistException.class, () -> bookService.modifyBook(bookId, request));
//
//        verify(bookRepository, times(1)).findById(bookId);
//        verify(bookStatusRepository, times(1)).findById(anyString());
//        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
//    }
}