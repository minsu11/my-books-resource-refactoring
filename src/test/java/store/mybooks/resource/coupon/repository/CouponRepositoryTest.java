package store.mybooks.resource.coupon.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.coupon.dto.response.CouponGetResponseForQuerydsl;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.coupon.repository
 * fileName       : CouponRepositoryTest
 * author         : damho-lee
 * date           : 3/9/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/9/24          damho-lee          최초 생성
 */
@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    CouponRepository couponRepository;

    @Autowired
    TestEntityManager testEntityManager;

    Coupon bookCoupon;

    Coupon totalCoupon;

    @BeforeEach
    void setup() {
        BookStatus bookStatus = new BookStatus("판매중");
        Publisher publisher = new Publisher("출판사");
        testEntityManager.persist(bookStatus);
        testEntityManager.persist(publisher);
        Book book = Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name("도서")
                .isbn("1234567898764")
                .publishDate(LocalDate.of(2024, 1, 1))
                .page(100)
                .index("인덱스")
                .content("내용")
                .originalCost(20000)
                .saleCost(16000)
                .discountRate(20)
                .stock(5)
                .isPackaging(true)
                .build();
        testEntityManager.persist(book);
        bookCoupon = new Coupon(
                "bookCoupon",
                book,
                null,
                0,
                null,
                5000,
                20,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                true,
                false
        );
        totalCoupon = new Coupon(
                "totalCoupon",
                null,
                null,
                0,
                5000,
                null,
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                false,
                true
        );
    }

    @Test
    @DisplayName("쿠폰 pageable 조회")
    void givenPageable_whenGetCoupons_thenReturnPageOfCouponGetResponseForQuerydsl() {
        couponRepository.save(bookCoupon);
        couponRepository.save(totalCoupon);
        Pageable pageable = PageRequest.of(0, 2);
        Page<CouponGetResponseForQuerydsl> couponGetResponseForQuerydslPage = couponRepository.getCoupons(pageable);

        assertThat(couponGetResponseForQuerydslPage).isNotNull();
        List<CouponGetResponseForQuerydsl> couponGetResponseForQuerydslList =
                couponGetResponseForQuerydslPage.getContent();
        assertThat(couponGetResponseForQuerydslList).isNotNull().hasSize(pageable.getPageSize());
        assertThat(couponGetResponseForQuerydslList.get(0).getId()).isEqualTo(bookCoupon.getId());
        assertThat(couponGetResponseForQuerydslList.get(1).getId()).isEqualTo(totalCoupon.getId());
    }

    @Test
    @DisplayName("쿠폰 pageable 조회 - 값이 없는 경우")
    void givenPageable_whenGetCouponsAndDatabaseHasNoData_thenReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<CouponGetResponseForQuerydsl> couponGetResponseForQuerydslPage = couponRepository.getCoupons(pageable);

        assertThat(couponGetResponseForQuerydslPage).isNotNull().isEmpty();
    }
}