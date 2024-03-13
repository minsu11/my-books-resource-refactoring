package store.mybooks.resource.book.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.request.BookModifyRequest;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookCartResponse;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookGetResponseForCoupon;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.dto.response.BookResponseForOrder;
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
import store.mybooks.resource.category.service.CategoryService;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.exception.ImageNotExistsException;
import store.mybooks.resource.image.repository.ImageRepository;
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
 * fileName       : BookService<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookStatusRepository bookStatusRepository;
    private final PublisherRepository publisherRepository;
    private final BookCategoryService bookCategoryService;
    private final BookTagService bookTagService;
    private final BookAuthorService bookAuthorService;
    private final BookMapper bookMapper;
    private final ImageService imageService;
    private final ImageStatusRepository imageStatusRepository;
    private final CategoryService categoryService;
    private final ImageRepository imageRepository;
    private final BookLikeRepository bookLikeRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    /**
     * methodName : getBookBriefInfo
     * author : newjaehun
     * description : 간략하게 전체 도서 정보 검색.
     *
     * @param pageable pageable
     * @return page
     */
    @Transactional(readOnly = true)
    public Page<BookBriefResponse> getBookBriefInfo(Pageable pageable) {
        return bookRepository.getBookBriefInfo(pageable);
    }

    /**
     * methodName : getActiveBookBriefInfo
     * author : newjaehun
     * description : 활성화된 간단 도서 정보 찾기.
     *
     * @param pageable pageable
     * @return page
     */
    @Transactional
    public Page<BookBriefResponse> getActiveBookBriefInfo(Pageable pageable) {
        return bookRepository.getActiveBookBriefInfo(pageable);
    }


    /**
     * methodName : getBookDetailInfo
     * author : newjaehun
     * description : 특정 도서 정보 검색.
     *
     * @param bookId 검색할 도서 ID
     * @return book detail response
     */
    @Transactional(readOnly = true)
    public BookDetailResponse getBookDetailInfo(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }
        BookDetailResponse response = bookRepository.getBookDetailInfo(bookId);
        response.setLikeCount(bookLikeRepository.countBookLikeByPk_BookId(bookId));
        response.setCategoryList(categoryService.getCategoryNameForBookView(bookId));
        return response;
    }

    /**
     * methodName : getBookForOrder
     * author : newjaehun
     * description : 주문에서 사용할 도서 정보.
     *
     * @param bookId 검색할 도서 ID
     * @return book response for order
     */
    @Transactional(readOnly = true)
    public BookResponseForOrder getBookForOrder(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }
        return bookRepository.getBookForOrder(bookId);

    }

    /**
     * methodName : createBook
     * author : newjaehun
     * description : 도서 추가하는 메서드.
     *
     * @param createRequest 추가할 도서의 정보 포함
     * @return bookCreateResponse : 추가된 도서의 name 포함
     */
    @Transactional
    public BookCreateResponse createBook(BookCreateRequest createRequest, MultipartFile thumbnail,
                                         List<MultipartFile> content)
            throws IOException {
        BookStatus bookStatus = bookStatusRepository.findById(createRequest.getBookStatusId())
                .orElseThrow(() -> new BookStatusNotExistException(createRequest.getBookStatusId()));

        Publisher publisher =
                publisherRepository.findById(createRequest.getPublisherId())
                        .orElseThrow(() -> new PublisherNotExistException(createRequest.getPublisherId()));

        if (bookRepository.existsByIsbn(createRequest.getIsbn())) {
            throw new IsbnAlreadyExistsException(createRequest.getIsbn());
        }

        Book book = Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name(createRequest.getName())
                .isbn(createRequest.getIsbn())
                .publishDate(createRequest.getPublishDate())
                .page(createRequest.getPage())
                .index(createRequest.getIndex())
                .explanation(createRequest.getExplanation())
                .originalCost(createRequest.getOriginalCost())
                .saleCost(createRequest.getSaleCost())
                .discountRate(createRequest.getOriginalCost() / createRequest.getSaleCost())
                .stock(createRequest.getStock())
                .viewCount(0)
                .isPackaging(createRequest.getIsPacking())
                .createdDate(LocalDate.now())
                .build();

        Book newBook = bookRepository.save(book);
        Long bookId = newBook.getId();
        bookAuthorService.createBookAuthor(new BookAuthorCreateRequest(bookId, createRequest.getAuthorList()));
        bookCategoryService.createBookCategory(new BookCategoryCreateRequest(bookId, createRequest.getCategoryList()));
        if (createRequest.getTagList() != null) {
            bookTagService.createBookTag(new BookTagCreateRequest(bookId, createRequest.getTagList()));
        }

        List<ImageRegisterResponse> imageRegisterResponseList = new ArrayList<>();
        ImageStatus thumbnailEnum = imageStatusRepository.findById(ImageStatusEnum.THUMBNAIL.getName()).orElseThrow(
                () -> new ImageStatusNotExistException("해당 하는 id의 이미지 상태가 없습니다."));
        ImageRegisterResponse imageRegisterResponse = imageService.saveImage(thumbnailEnum, null, book, thumbnail);
        imageRegisterResponseList.add(imageRegisterResponse);

        ImageStatus contentEnum = imageStatusRepository.findById(ImageStatusEnum.CONTENT.getName())
                .orElseThrow(() -> new ImageStatusNotExistException("해당 하는 id의 이미지 상태가 없습니다."));
        for (MultipartFile file : content) {
            imageRegisterResponseList.add(imageService.saveImage(contentEnum, null, book, file));
        }
        return bookMapper.createResponse(newBook);
    }


    /**
     * methodName : modifyBook
     * author : newjaehun
     * description : 도서 수정하는 메서드.
     *
     * @param bookId        수정할 도서 ID
     * @param modifyRequest 수정할 도서의 정보 포함
     * @return book modify response
     */
    @Transactional
    public BookModifyResponse modifyBook(Long bookId, BookModifyRequest modifyRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotExistException(bookId));

        BookStatus bookStatus = bookStatusRepository.findById(modifyRequest.getBookStatusId())
                .orElseThrow(() -> new BookStatusNotExistException(modifyRequest.getBookStatusId()));

        book.setModifyRequest(bookStatus, modifyRequest.getSaleCost(),
                ((book.getOriginalCost() - modifyRequest.getSaleCost()) * 100) / book.getOriginalCost(),
                modifyRequest.getStock(), modifyRequest.getIsPacking());

        bookCategoryService.deleteBookCategory(bookId);
        bookCategoryService.createBookCategory(new BookCategoryCreateRequest(bookId, modifyRequest.getCategoryList()));

        bookTagService.deleteBookTag(bookId);
        if (modifyRequest.getTagList() != null) {
            bookTagService.createBookTag(new BookTagCreateRequest(bookId, modifyRequest.getTagList()));
        }
        return bookMapper.modifyResponse(book);
    }

    /**
     * methodName : getBookInCart
     * author : Fiat_lux
     * description : 장바구니 안에 있는 책의 필요한 정보 가져오는 메서드
     *
     * @param bookId the book id
     * @return BookCartResponse dto
     */
    @Transactional(readOnly = true)
    public BookCartResponse getBookInCart(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));
        Image image = imageRepository.findImageByBook_IdAndImageStatus_Id(bookId, ImageStatusEnum.THUMBNAIL.getName())
                .orElseThrow(() -> new ImageNotExistsException("해당하는 id의 이미지가 없습니다"));


        String url = image.getPath() + image.getFileName() + image.getExtension();
        return new BookCartResponse(book.getId(), book.getName(), url, book.getSaleCost());
    }

    @Transactional(readOnly = true)
    public List<BookGetResponseForCoupon> getBookForCoupon() {
        return bookRepository.getBookForCoupon();
    }

    /**
     * methodName : updateBookViewCount
     * author : newjaehun
     * description : 스케쥴러를 이용하여 조횟수 업데이트.
     */
    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void updateBookViewCount() {
        Set<String> keys = redisTemplate.keys("viewCount:*");
        if (keys != null) {
            for (String key : keys) {
                Long bookId = Long.parseLong(key.substring("viewCount:".length()));
                if (!bookRepository.existsById(bookId)) {
                    throw new BookNotExistException(bookId);
                }
                Integer viewCount = redisTemplate.opsForValue().get(key);
                if (viewCount != null) {
                    bookRepository.updateBookViewCount(bookId, viewCount);
                    redisTemplate.delete(key);
                }
            }
        }
    }
}
