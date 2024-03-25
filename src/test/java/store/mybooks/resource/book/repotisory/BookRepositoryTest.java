package store.mybooks.resource.book.repotisory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookGetResponseForCoupon;
import store.mybooks.resource.book.dto.response.BookPopularityResponse;
import store.mybooks.resource.book.dto.response.BookResponseForOrder;
import store.mybooks.resource.book.dto.response.BookStockResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.utils.TimeUtils;

/**
 * packageName    : store.mybooks.resource.book.repotisory <br/>
 * fileName       : BookRepositoryTest<br/>
 * author         : newjaehun <br/>
 * date           : 3/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/25/24        newjaehun       최초 생성<br/>
 */
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Long bookId;

    @BeforeEach
    void setUp() {
        Publisher publisher = new Publisher("출판사명");
        entityManager.persist(publisher);

        BookStatus bookStatus = new BookStatus("상태명");
        entityManager.persist(bookStatus);

        Book book = Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name("도서명")
                .isbn("1234567890123")
                .publishDate(LocalDate.of(2024, 1, 1))
                .page(100)
                .index("index")
                .explanation("content")
                .originalCost(20000)
                .saleCost(16000)
                .discountRate(20)
                .stock(5)
                .createdDate(TimeUtils.nowDate())
                .viewCount(1)
                .isPackaging(true)
                .build();

        book = entityManager.persist(book);
        entityManager.flush();

        bookId = book.getId();
    }

    @Test
    @DisplayName("ISBN 이 이미 있는 경우")
    void givenIsbn_whenExistsByIsbn_thenReturnTrue() {
        String isbn = "1234567890";
        Book book = Book.builder()
                .isbn(isbn)
                .build();
        bookRepository.save(book);

        assertThat(bookRepository.existsByIsbn(isbn)).isTrue();
    }

    @Test
    @DisplayName("ISBN 이 없는 경우")
    void givenExistIsbn_whenExistsByIsbn_thenReturnFalse() {
        String existingIsbn = "1234567890";

        assertThat(bookRepository.existsByIsbn(existingIsbn)).isFalse();
    }

    @Test
    @DisplayName("도서 상세 정보")
    void givenBookId_whenGetBookDetailInfo_thenReturnBookDetailResponse() {
        BookDetailResponse result = bookRepository.getBookDetailInfo(bookId);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookId);
    }

    @Test
    @DisplayName("쿠폰 발급시 사용할 도서 목록")
    void whenGetBookForCoupon_thenBookGetResponseForCoupon() {
        BookStatus onSaleStatus = new BookStatus("판매중");
        entityManager.persist(onSaleStatus);

        BookStatus outOfStockStatus = new BookStatus("재고없음");
        entityManager.persist(outOfStockStatus);

        Book bookOnSale = Book.builder()
                .name("판매중인 책")
                .isbn("1234567890")
                .bookStatus(onSaleStatus)
                .build();
        entityManager.persist(bookOnSale);

        Book bookOutOfStock = Book.builder()
                .name("재고없는 책")
                .isbn("0987654321")
                .bookStatus(outOfStockStatus)
                .build();
        entityManager.persist(bookOutOfStock);

        BookStatus otherStatus = new BookStatus("삭제도서");
        entityManager.persist(otherStatus);

        Book bookOtherStatus = Book.builder()
                .name("삭제된 책")
                .isbn("1122334455")
                .bookStatus(otherStatus)
                .build();
        entityManager.persist(bookOtherStatus);

        List<BookGetResponseForCoupon> result = bookRepository.getBookForCoupon();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(BookGetResponseForCoupon::getName)
                .containsExactlyInAnyOrder("판매중인 책", "재고없는 책");
    }

    @Test
    @DisplayName("조회수 업데이트")
    void whenUpdateBookViewCount() {
        Book book = Book.builder()
                .name("도서명")
                .isbn("1234567890123")
                .viewCount(0)
                .build();
        book = entityManager.persistFlushFind(book);

        Integer increaseCount = 5;
        bookRepository.updateBookViewCount(book.getId(), increaseCount);

        entityManager.refresh(book);

        assertThat(book.getViewCount()).isEqualTo(increaseCount);
    }

    @Test
    @DisplayName("도서 재고 조회")
    void givenBookId_whenGetBookStockList_thenReturnBookStockResponse() {
        Book book = Book.builder()
                .name("도서명")
                .stock(5)
                .build();
        book = entityManager.persistFlushFind(book);

        BookStockResponse response = bookRepository.getBookStockList(book.getId());

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(book.getId());
        assertThat(response.getStock()).isEqualTo(book.getStock());
    }

    @Test
    @DisplayName("주문에 사용할 도서 정보")
    void givenBookId_whenGetBookForOrder_thenReturnBookResponseForOrder() {
        BookStatus bookStatus = new BookStatus("판매중");
        entityManager.persist(bookStatus);

        ImageStatus imageStatus = new ImageStatus("썸네일");
        entityManager.persist(imageStatus);

        Book book = Book.builder()
                .name("도서명")
                .saleCost(20000)
                .originalCost(25000)
                .discountRate(20)
                .isPackaging(true)
                .stock(10)
                .bookStatus(bookStatus)
                .build();
        entityManager.persist(book);

        Image image = new Image("/", "thumbnail", ".jpg", book, null, imageStatus);
        entityManager.persist(image);

        Long bookId = book.getId();

        BookResponseForOrder result = bookRepository.getBookForOrder(bookId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookId);
        assertThat(result.getName()).isEqualTo("도서명");
        assertThat(result.getBookImage()).isEqualTo("/thumbnail.jpg");
        assertThat(result.getSaleCost()).isEqualTo(20000);
        assertThat(result.getOriginalCost()).isEqualTo(25000);
        assertThat(result.getDisCountRate()).isEqualTo(20);
        assertThat(result.getIsPacking()).isTrue();
        assertThat(result.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("인기순으로 정렬된 도서 리스트")
    void whenGetBookPopularity_thenReturnBookPopularityResponseList() {
        BookStatus sellingStatus = new BookStatus("판매중");
        entityManager.persist(sellingStatus);

        BookStatus outOfStockStatus = new BookStatus("재고없음");
        entityManager.persist(outOfStockStatus);

        ImageStatus thumbnailStatus = new ImageStatus("썸네일");
        entityManager.persist(thumbnailStatus);

        Book book1 = Book.builder()
                .name("도서1")
                .originalCost(30000)
                .saleCost(25000)
                .viewCount(10)
                .bookStatus(sellingStatus)
                .build();
        entityManager.persist(book1);

        Book book2 = Book.builder()
                .name("도서2")
                .originalCost(35000)
                .saleCost(28000)
                .viewCount(15)
                .bookStatus(outOfStockStatus)
                .build();
        entityManager.persist(book2);

        Image image1 = new Image("/", "thumbnail1", ".jpg", book1, null, thumbnailStatus);
        entityManager.persist(image1);

        Image image2 = new Image("/", "thumbnail2", ".jpg", book2, null, thumbnailStatus);
        entityManager.persist(image2);

        List<BookPopularityResponse> result = bookRepository.getBookPopularity();

        assertThat(result).hasSize(2);

        BookPopularityResponse response1 = result.get(0);
        assertThat(response1.getId()).isEqualTo(book2.getId());
        assertThat(response1.getImage()).isEqualTo("/thumbnail2.jpg");
        assertThat(response1.getName()).isEqualTo("도서2");
        assertThat(response1.getReviewCount()).isEqualTo(0L);
        assertThat(response1.getCost()).isEqualTo(35000);
        assertThat(response1.getSaleCost()).isEqualTo(28000);
        assertThat(response1.getRate()).isEqualTo(0.0);
        assertThat(response1.getViewCount()).isEqualTo(15);

        BookPopularityResponse response2 = result.get(1);
        assertThat(response2.getId()).isEqualTo(book1.getId());
        assertThat(response2.getImage()).isEqualTo("/thumbnail1.jpg");
        assertThat(response2.getName()).isEqualTo("도서1");
        assertThat(response2.getReviewCount()).isEqualTo(0L);
        assertThat(response2.getCost()).isEqualTo(30000);
        assertThat(response2.getSaleCost()).isEqualTo(25000);
        assertThat(response2.getRate()).isEqualTo(0.0);
        assertThat(response2.getViewCount()).isEqualTo(10);
    }

}