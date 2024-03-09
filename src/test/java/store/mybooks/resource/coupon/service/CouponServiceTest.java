package store.mybooks.resource.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.util.ReflectionTestUtils;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.book_status.entity.BookStatus;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.repository.CategoryRepository;
import store.mybooks.resource.coupon.dto.request.CouponCreateRequest;
import store.mybooks.resource.coupon.dto.response.CouponGetResponse;
import store.mybooks.resource.coupon.dto.response.CouponGetResponseForQuerydsl;
import store.mybooks.resource.coupon.exception.CouponCannotDeleteException;
import store.mybooks.resource.coupon.exception.CouponDateIncorrectException;
import store.mybooks.resource.coupon.exception.CouponInCompatibleType;
import store.mybooks.resource.coupon.exception.CouponNotExistsException;
import store.mybooks.resource.coupon.exception.OrderMinLessThanDiscountCostException;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.usercoupon.repository.UserCouponRepository;

/**
 * packageName    : store.mybooks.resource.coupon.service
 * fileName       : CouponServiceTest
 * author         : damho-lee
 * date           : 3/7/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/24          damho-lee          최초 생성
 */
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    @Mock
    CouponRepository couponRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserCouponRepository userCouponRepository;

    @InjectMocks
    CouponService couponService;

    @Test
    @DisplayName("쿠폰 페이지 요청 테스트")
    void givenPageable_whenRequestCouponAdminPage_thenReturnPageOfCoupon() {
        List<CouponGetResponseForQuerydsl> couponList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 3);
        CouponGetResponseForQuerydsl firstCoupon =
                new CouponGetResponseForQuerydsl(
                        1L,
                        "firstCoupon",
                        10000,
                        0,
                        3000,
                        40,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );

        CouponGetResponseForQuerydsl secondCoupon =
                new CouponGetResponseForQuerydsl(
                        2L,
                        "secondCoupon",
                        10000,
                        1000,
                        0,
                        0,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );

        CouponGetResponseForQuerydsl thirdCoupon =
                new CouponGetResponseForQuerydsl(
                        3L,
                        "thirdCoupon",
                        10000,
                        1000,
                        0,
                        0,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );

        CouponGetResponseForQuerydsl fourthCoupon =
                new CouponGetResponseForQuerydsl(
                        4L,
                        "fourthCoupon",
                        10000,
                        1000,
                        0,
                        0,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );

        CouponGetResponseForQuerydsl fifthCoupon =
                new CouponGetResponseForQuerydsl(
                        5L,
                        "fifthCoupon",
                        5000,
                        1000,
                        0,
                        0,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        "빙하기에서 살아남기",
                        null
                );

        CouponGetResponseForQuerydsl sixthCoupon =
                new CouponGetResponseForQuerydsl(
                        6L,
                        "sixthCoupon",
                        10000,
                        0,
                        3000,
                        20,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        "위인전"
                );
        couponList.add(firstCoupon);
        couponList.add(secondCoupon);
        couponList.add(thirdCoupon);
        couponList.add(fourthCoupon);
        couponList.add(fifthCoupon);
        couponList.add(sixthCoupon);
        int offset = (int) pageable.getOffset();
        Page<CouponGetResponseForQuerydsl> couponPage =
                new PageImpl<>(couponList.subList(offset, offset + pageable.getPageSize()), pageable,
                        couponList.size());
        when(couponRepository.getCoupons(any())).thenReturn(couponPage);

        Page<CouponGetResponse> actualPage = couponService.getCoupons(pageable);
        List<CouponGetResponse> actualList = actualPage.getContent();

        assertThat(actualPage).isNotNull();
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getId()).isEqualTo(fourthCoupon.getId());
        assertThat(actualList.get(1).getId()).isEqualTo(fifthCoupon.getId());
        assertThat(actualList.get(2).getId()).isEqualTo(sixthCoupon.getId());
        assertThat(actualPage.getPageable().getPageNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getPageable().getPageSize()).isEqualTo(pageable.getPageSize());
        assertThat(actualPage.getTotalElements()).isEqualTo(couponList.size());
    }

    @Test
    @DisplayName("전체 쿠폰 생성 성공")
    void givenCouponCreateRequest_whenCreateCoupon_thenCreateCoupon() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().minusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        couponService.createCoupon(couponCreateRequest);

        verify(couponRepository, times(1)).save(any());
        verify(bookRepository, times(0)).findById(anyLong());
        verify(categoryRepository, times(0)).findById(anyInt());
    }

    @Test
    @DisplayName("도서 쿠폰 생성 성공")
    void givenCouponCreateRequestThatHasBookId_whenCreateCoupon_thenCreateCoupon() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", 1L);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().minusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        BookStatus bookStatus = new BookStatus("판매중");
        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        Book book =
                new Book(1L, bookStatus, publisher, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        couponService.createCoupon(couponCreateRequest);

        verify(couponRepository, times(1)).save(any());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(categoryRepository, times(0)).findById(anyInt());
    }

    @Test
    @DisplayName("카테고리 쿠폰 생성 성공")
    void givenCouponCreateRequestThatHasCategoryId_whenCreateCoupon_thenCreateCoupon() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", 1);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().minusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        Category category = new Category(1, null, null, "카테고리", LocalDate.now());

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        couponService.createCoupon(couponCreateRequest);

        verify(couponRepository, times(1)).save(any());
        verify(bookRepository, times(0)).findById(anyLong());
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - 시작일이 종료일 보다 나중인 경우")
    void givenCouponCreateRequestThatHasIncorrectCouponDate_whenCreateCoupon_thenThrowCouponDateIncorrectException() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().plusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().minusDays(3));

        assertThrows(CouponDateIncorrectException.class, () -> couponService.createCoupon(couponCreateRequest));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - 쿠폰 타입 validation 실패(정률, 정액 쿠폰 판단이 모호한 경우 = discountCost 와 discountRate 둘 다 설정된 경우)")
    void givenCouponCreateRequestThatHasIncompatibleCouponType_whenCreateCoupon_thenThrowCouponIncompatibleType() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().minusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        assertThrows(CouponInCompatibleType.class, () -> couponService.createCoupon(couponCreateRequest));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - 쿠폰 대상 validation 실패(bookId, categoryId 둘 다 설정된 경우)")
    void givenCouponCreateRequestThatHasBothBookIdAndCategoryId_whenCreateCoupon_thenThrowCouponIncompatibleType() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", 1L);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", 1);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().minusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        assertThrows(CouponInCompatibleType.class, () -> couponService.createCoupon(couponCreateRequest));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - 쿠폰 최소 주문 금액이 할인 금액보다 작은 경우")
    void givenCouponCreateRequestThatHasOrderMinLessThanDiscountCost_whenCreateCoupon_thenThrowOrderMinLessThanDiscountCostException() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", 10001);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", null);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().minusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        assertThrows(OrderMinLessThanDiscountCostException.class,
                () -> couponService.createCoupon(couponCreateRequest));
    }

    @Test
    @DisplayName("쿠폰 삭제 성공")
    void givenCouponId_whenDeleteCoupon_thenDoNotReturnAnyThing() {
        when(couponRepository.existsById(anyLong())).thenReturn(true);
        when(userCouponRepository.countByCoupon_Id(anyLong())).thenReturn(0);
        doNothing().when(couponRepository).deleteById(anyLong());

        couponService.deleteCoupon(1L);

        verify(couponRepository, times(1)).existsById(anyLong());
        verify(userCouponRepository, times(1)).countByCoupon_Id(anyLong());
        verify(couponRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("쿠폰 삭제 실패 - 존재하지 않는 쿠폰 아이디")
    void givenNotExistsCouponId_whenDeleteCoupon_thenThrowCouponNotExistsException() {
        when(couponRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(CouponNotExistsException.class, () -> couponService.deleteCoupon(1L));
    }

    @Test
    @DisplayName("쿠폰 삭제 실패 - 회원들에게 배포된 쿠폰인 경우")
    void givenAlreadyDistributedCouponId_whenDeleteCoupon_thenThrowCouponCannotDeleteException() {
        when(couponRepository.existsById(anyLong())).thenReturn(true);
        when(userCouponRepository.countByCoupon_Id(anyLong())).thenReturn(1);

        assertThrows(CouponCannotDeleteException.class, () -> couponService.deleteCoupon(1L));
    }
}