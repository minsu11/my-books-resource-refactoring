package store.mybooks.resource.book.controller;

import java.io.IOException;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.request.BookModifyRequest;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookCartResponse;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookGetResponseForCoupon;
import store.mybooks.resource.book.dto.response.BookLikeResponse;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.dto.response.BookPopularityResponse;
import store.mybooks.resource.book.dto.response.BookPublicationDateResponse;
import store.mybooks.resource.book.dto.response.BookRatingResponse;
import store.mybooks.resource.book.dto.response.BookResponseForOrder;
import store.mybooks.resource.book.dto.response.BookReviewResponse;
import store.mybooks.resource.book.dto.response.BookStockResponse;
import store.mybooks.resource.book.service.BookService;
import store.mybooks.resource.error.Utils;

/**
 * packageName    : store.mybooks.resource.book.controller <br/>
 * fileName       : BookRestController<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookService bookService;

    /**
     * methodName : getBookBrief
     * author : newjaehun
     * description : 간략한 전체 도서 리스트 반환.
     *
     * @param pageable pageable
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<Page<BookBriefResponse>> getBookBrief(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookBriefInfo(pageable));
    }

    /**
     * methodName : getBookDetail
     * author : newjaehun
     * description : 도서 상세보기.
     *
     * @param bookId 조회하려는 도서 ID
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> getBookDetail(@PathVariable("id") Long bookId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookDetailInfo(bookId));
    }

    /**
     * methodName : getBookForOrder
     * author : newjaehun
     * description : 주문에서 사용할 도서 정보.
     *
     * @param bookId 조회하려는 도서 ID
     * @return response entity
     */
    @GetMapping("/{id}/order")
    public ResponseEntity<BookResponseForOrder> getBookForOrder(@PathVariable("id") Long bookId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookForOrder(bookId));
    }

    /**
     * methodName : getBookForCoupon
     * author : newjaehun
     * description : 쿠폰 생성에서 사용할 도서 목록.
     *
     * @return responseEntity
     */
    @GetMapping("/for-coupon")
    public ResponseEntity<List<BookGetResponseForCoupon>> getBookForCoupon() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookForCoupon());
    }

    @GetMapping("/popularity")
    public ResponseEntity<List<BookPopularityResponse>> getBookPopularity() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookPopularityList());
    }

    @GetMapping("/bookLike")
    public ResponseEntity<List<BookLikeResponse>> getBookLike() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookLikeList());
    }

    @GetMapping("/bookReviewCount")
    public ResponseEntity<List<BookReviewResponse>> getBookReview() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookReviewList());
    }

    @GetMapping("/bookRating")
    public ResponseEntity<List<BookRatingResponse>> getBookRating() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookRatingList());
    }

    @GetMapping("/bookPublicationDate")
    public ResponseEntity<List<BookPublicationDateResponse>> getBookPublicationDate() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookPublicationDateList());
    }


    /**
     * methodName : createBook
     * author : newjaehun
     * description : 도서 추가.
     *
     * @param createRequest 추가하려는 도서 정보 포함
     * @param bindingResult bindingResult
     * @return responseEntity
     * @throws BindException the bind exception
     */
    @PostMapping
    public ResponseEntity<BookCreateResponse> createBook(@Valid @RequestPart("request") BookCreateRequest createRequest,
                                                         @RequestPart("thumbnail") MultipartFile thumbnail,
                                                         @RequestPart("content") List<MultipartFile> content,
                                                         BindingResult bindingResult)
            throws IOException {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.createBook(createRequest, thumbnail, content));
    }

    /**
     * methodName : modifyBook
     * author : newjaehun
     * description : 도서 수정.
     *
     * @param bookId        수정하려는 도서 ID
     * @param modifyRequest 수정하려는 도서 정보 포함
     * @param bindingResult bindingResult
     * @return responseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookModifyResponse> modifyBook(@PathVariable("id") Long bookId,
                                                         @Valid @RequestPart("request") BookModifyRequest modifyRequest,
                                                         @RequestPart(value = "thumbnail", required = false)
                                                         MultipartFile thumbnail,
                                                         @RequestPart(value = "content", required = false)
                                                         List<MultipartFile> content,
                                                         BindingResult bindingResult) throws IOException {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.modifyBook(bookId, modifyRequest, thumbnail, content));
    }

    @GetMapping("/cart-books/{id}")
    public ResponseEntity<BookCartResponse> getBookInCart(@PathVariable("id") Long bookId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookInCart(bookId));
    }

    /**
     * methodName : getBookStock
     * author : minsu11
     * description : {@code bookId}인 도서의 재고.
     *
     * @param bookId the book id
     * @return the book stock
     */
    @GetMapping("/{id}/order/stock")
    public ResponseEntity<BookStockResponse> getBookStock(@PathVariable("id") Long bookId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookService.getBookStockResponse(bookId));
    }
}
