package store.mybooks.resource.book.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.request.BookModifyRequest;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.dto.response.BookPublicationDateResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.exception.IsbnAlreadyExistsException;
import store.mybooks.resource.book.mapper.BookMapper;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookauthor.dto.request.BookAuthorCreateRequest;
import store.mybooks.resource.bookauthor.service.BookAuthorService;
import store.mybooks.resource.bookcategory.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.bookcategory.service.BookCategoryService;
import store.mybooks.resource.booklike.repository.BookLikeRepository;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.bookstatus.exception.BookStatusNotExistException;
import store.mybooks.resource.bookstatus.respository.BookStatusRepository;
import store.mybooks.resource.booktag.dto.request.BookTagCreateRequest;
import store.mybooks.resource.booktag.service.BookTagService;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
import store.mybooks.resource.category.service.CategoryService;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.service.ImageService;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.image_status.exception.ImageStatusNotExistException;
import store.mybooks.resource.image_status.repository.ImageStatusRepository;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.publisher.repository.PublisherRepository;
import store.mybooks.resource.tag.dto.response.TagGetResponseForBookDetail;
import store.mybooks.resource.utils.TimeUtils;

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
    public BookLikeRepository bookLikeRepository;

    @Mock
    public CategoryService categoryService;

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

    @Test
    @DisplayName("도서 수정")
    void givenBookIdAndBookModifyRequest_whenModifyBook_thenModifyBookAndReturnBookModifyResponse() throws IOException {
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        Long bookId = 2L;

        Book book =
                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
                        "1234567898763",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        BookModifyResponse response = new BookModifyResponse();
        response.setName(book.getName());

        BookStatus bookStatus = new BookStatus("판매종료");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        given(publisherRepository.findById(anyInt())).willReturn(Optional.of(publisher));

        given(bookRepository.existsByIsbn(anyString())).willReturn(false);

        doNothing().when(bookAuthorService).deleteBookAuthor(bookId);
        doNothing().when(bookAuthorService).createBookAuthor(any(BookAuthorCreateRequest.class));
        doNothing().when(bookCategoryService).deleteBookCategory(bookId);
        doNothing().when(bookCategoryService).createBookCategory(any(BookCategoryCreateRequest.class));
        doNothing().when(bookTagService).deleteBookTag(bookId);
        doNothing().when(bookTagService).createBookTag(any(BookTagCreateRequest.class));
        doNothing().when(imageService).updateImage(any(Book.class), eq(null), eq(null));

        when(bookMapper.modifyResponse(any(Book.class))).thenReturn(response);

        bookService.modifyBook(bookId, request, null, null);

        Assertions.assertThat(response.getName()).isEqualTo(book.getName());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookMapper, times(1)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정(존재하지 않는 도서)")
    void givenNotExistsBookId_whenModifyBook_thenThrowBookNotExistException() throws IOException {
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        Long bookId = 2L;

        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        assertThrows(BookNotExistException.class, () -> bookService.modifyBook(bookId, request, null, null));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(0)).findById(anyString());
        verify(publisherRepository, times(0)).findById(anyInt());
        verify(bookRepository, times(0)).existsByIsbn(anyString());
        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정(존재하지 않는 도서상태)")
    void givenNotExistBookStatus_whenModifyBook_thenThrowBookStatusNotExistException() throws IOException {
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        Long bookId = 2L;

        Book book =
                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
                        "1234567898763",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        given(bookStatusRepository.findById(anyString())).willReturn(Optional.empty());

        assertThrows(BookStatusNotExistException.class, () -> bookService.modifyBook(bookId, request, null, null));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(0)).findById(anyInt());
        verify(bookRepository, times(0)).existsByIsbn(anyString());
        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정(존재하지 않는 출판사)")
    void givenNotExistPublisher_whenModifyBook_thenThrowPublisherNotExistException() throws IOException {
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        Long bookId = 2L;

        Book book =
                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
                        "1234567898763",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        BookModifyResponse response = new BookModifyResponse();
        response.setName(book.getName());

        BookStatus bookStatus = new BookStatus("판매종료");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        given(publisherRepository.findById(anyInt())).willReturn(Optional.empty());

        assertThrows(PublisherNotExistException.class, () -> bookService.modifyBook(bookId, request, null, null));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(0)).existsByIsbn(anyString());
        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("도서 수정(이미 존재하는 ISBN 으로 수정)")
    void givenAlreadyExistsIsbn_whenModifyBook_thenThrowIsbnAlreadyExistsException() throws IOException {
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        Long bookId = 2L;

        Book book =
                new Book(bookId, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1",
                        "1234567898763",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        BookModifyResponse response = new BookModifyResponse();
        response.setName(book.getName());

        BookStatus bookStatus = new BookStatus("판매종료");
        given(bookStatusRepository.findById(anyString())).willReturn(Optional.of(bookStatus));

        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        given(publisherRepository.findById(anyInt())).willReturn(Optional.of(publisher));

        given(bookRepository.existsByIsbn(anyString())).willReturn(true);

        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.modifyBook(bookId, request, null, null));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookStatusRepository, times(1)).findById(anyString());
        verify(publisherRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookMapper, times(0)).modifyResponse(any(Book.class));
    }

    @Test
    @DisplayName("출판일로 정렬된 도서 리스트")
    void whenGetBookPublicationDateList_thenReturnBookPublicationDateResponseList() {
        List<BookPublicationDateResponse> responses = List.of(
                new BookPublicationDateResponse(1L, "image", "name", 0L, 1000, 1000, 0.0, TimeUtils.nowDate()),
                new BookPublicationDateResponse(2L, "image1", "name2", 1L, 2000, 2000, 1.0, TimeUtils.nowDate())
        );

        when(bookRepository.getBookPublicationDate()).thenReturn(responses);

        List<BookPublicationDateResponse> result = bookService.getBookPublicationDateList();

        assertThat(result).isEqualTo(responses);
        verify(bookRepository, times(1)).getBookPublicationDate();
    }

    @Test
    @DisplayName("페이징된 도서 미리보기")
    void givenPageable_whenGetBookBriefInfo_thenReturnPagedBookBriefResponse() {
        int page = 0, size = 2;
        Pageable pageable = PageRequest.of(page, size);

        BookBriefResponse response1 = new BookBriefResponse(1L, "imageUrl1", "name1", 1.0, 1L, 10000, 9000);
        BookBriefResponse response2 = new BookBriefResponse(2L, "imageUrl2", "name2", 2.0, 2L, 20000, 19000);

        Page<BookBriefResponse> expectedResponse = new PageImpl<>(Arrays.asList(response1, response2), pageable, 2);

        when(bookRepository.getBookBriefInfo(pageable)).thenReturn(expectedResponse);

        Page<BookBriefResponse> result = bookService.getBookBriefInfo(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("id").containsExactly(1L, 2L);
        assertThat(result.getContent()).extracting("name").containsExactly("name1", "name2");

        verify(bookRepository, times(1)).getBookBriefInfo(any(Pageable.class));
    }

    @Test
    @DisplayName("도서 상세 정보(존재하지 않는 도서 ID)")
    void givenNotExistsBookId_whenGetBookDetailInfo_thenThrowBookNotExistException() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);
        assertThrows(BookNotExistException.class, () -> bookService.getBookDetailInfo(bookId));

        verify(bookRepository, times(1)).existsById(bookId);
    }

    @Test
    @DisplayName("도서 상세 정보")
    void givenBookId_whenGetBookDetailInfo_thenReturnBookDetailResponse() {
        Long bookId = 1L;
        BookDetailResponse response = BookDetailResponse.builder()
                .id(bookId)
                .thumbNailImage("thumbnailImagePath")
                .name("Book Name")
                .bookStatus("판매중")
                .authorList(List.of(new AuthorGetResponse(1, "Author Name", "Author Content")))
                .publisher(new PublisherGetResponse(1, "Publisher Name"))
                .publishDate(LocalDate.of(2024, 1, 1))
                .saleCost(15000)
                .originalCost(20000)
                .disCountRate(25)
                .rate(4.5)
                .reviewCount(100L)
                .isPacking(true)
                .page(300)
                .isbn("1234567890123")
                .tagList(List.of(new TagGetResponseForBookDetail(1, "TagName")))
                .stock(50)
                .index("Book Index")
                .explanation("Book Explanation")
                .contentImageList(List.of("imagePath1", "imagePath2"))
                .build();

        response.setLikeCount(10);
        response.setCategoryList(List.of(new CategoryIdNameGetResponse(1, "Category Name")));
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.getBookDetailInfo(bookId)).thenReturn(response);
        when(bookLikeRepository.countBookLikeByPk_BookId(bookId)).thenReturn(response.getLikeCount());
        when(categoryService.getCategoryNameForBookView(bookId)).thenReturn(response.getCategoryList());

        BookDetailResponse actualResponse = bookService.getBookDetailInfo(bookId);

        assertThat(actualResponse.getName()).isEqualTo(response.getName());
        assertThat(actualResponse.getLikeCount()).isEqualTo(response.getLikeCount());
        assertThat(actualResponse.getCategoryList()).usingRecursiveFieldByFieldElementComparator()
                .containsAll(response.getCategoryList());

        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).getBookDetailInfo(bookId);
        verify(bookLikeRepository, times(1)).countBookLikeByPk_BookId(bookId);
    }

}