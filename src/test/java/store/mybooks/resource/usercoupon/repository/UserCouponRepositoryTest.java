package store.mybooks.resource.usercoupon.repository;

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
import store.mybooks.resource.book_category.entity.BookCategory;
import store.mybooks.resource.book_status.entity.BookStatus;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForMyPageQuerydsl;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForOrderQuerydsl;
import store.mybooks.resource.usercoupon.entity.UserCoupon;

/**
 * packageName    : store.mybooks.resource.usercoupon.repository
 * fileName       : UserCouponRepositoryTest
 * author         : damho-lee
 * date           : 3/9/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/9/24          damho-lee          최초 생성
 */
@DataJpaTest
class UserCouponRepositoryTest {
    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    TestEntityManager testEntityManager;

    UserCoupon userBookCoupon;
    UserCoupon userTotalCoupon;
    UserCoupon userCategoryCoupon;

    UserGradeName userGradeName;

    UserStatus userStatus;

    UserGrade userGrade;

    User user;

    Book book;

    Coupon bookCoupon;

    Coupon totalCoupon;

    Coupon categoryCoupon;

    BookCategory bookCategory;

    @BeforeEach
    void setup() {
        BookStatus bookStatus = new BookStatus("판매중");
        testEntityManager.persist(bookStatus);

        Publisher publisher = new Publisher("출판사");
        testEntityManager.persist(publisher);

        book =
                new Book(bookStatus, publisher, "도서", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스",
                        "내용", 20000, 16000, 20, 5, true);
        testEntityManager.persist(book);

        userStatus = new UserStatus("활동중");
        testEntityManager.persist(userStatus);

        userGradeName = new UserGradeName("브론즈");
        testEntityManager.persist(userGradeName);

        userGrade = new UserGrade(
                10000,
                100000,
                3,
                LocalDate.now(),
                userGradeName
        );
        testEntityManager.persist(userGrade);

        user = new User(
                "test@naver.com",
                2010,
                "1010",
                "password",
                "01011112222",
                false,
                "신재훈",
                userStatus,
                userGrade);
        testEntityManager.persist(user);

        Category category = new Category(null, "카테고리");
        testEntityManager.persist(category);

        bookCoupon = new Coupon(
                "bookCoupon",
                book,
                null,
                0,
                null,
                5000,
                20,
                LocalDate.now().minusDays(1),
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
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(3),
                false,
                true
        );
        categoryCoupon = new Coupon(
                "categoryCoupon",
                null,
                category,
                0,
                5000,
                null,
                null,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(3),
                false,
                true
        );
        testEntityManager.persist(bookCoupon);
        testEntityManager.persist(totalCoupon);
        testEntityManager.persist(categoryCoupon);

        bookCategory = new BookCategory(
                new BookCategory.Pk(book.getId(), category.getId()),
                book,
                category
        );
        testEntityManager.persist(bookCategory);

        userBookCoupon = new UserCoupon(user, bookCoupon);
        userTotalCoupon = new UserCoupon(user, totalCoupon);
        userCategoryCoupon = new UserCoupon(user, categoryCoupon);
    }

    @Test
    @DisplayName("회원 쿠폰 조회")
    void givenUserIdAndPageable_whenGetUserCoupons_thenReturnPageOfUserCouponGetResponseForMyPageQuerydsl() {
        userCouponRepository.save(userBookCoupon);
        userCouponRepository.save(userTotalCoupon);
        userCouponRepository.save(userCategoryCoupon);
        Pageable pageable = PageRequest.of(0, 2);
        Page<UserCouponGetResponseForMyPageQuerydsl> userCouponGetResponseForMyPageQuerydslPage =
                userCouponRepository.getUserCoupons(user.getId(), pageable);
        List<UserCouponGetResponseForMyPageQuerydsl> userCouponGetResponseForMyPageQuerydslList =
                userCouponGetResponseForMyPageQuerydslPage.getContent();

        assertThat(userCouponGetResponseForMyPageQuerydslPage).isNotNull();
        assertThat(userCouponGetResponseForMyPageQuerydslList).isNotNull().hasSize(pageable.getPageSize());
        assertThat(userCouponGetResponseForMyPageQuerydslList.get(0).getId()).isEqualTo(userBookCoupon.getId());
        assertThat(userCouponGetResponseForMyPageQuerydslList.get(1).getId()).isEqualTo(userTotalCoupon.getId());
    }

    @Test
    @DisplayName("회원 쿠폰 조회 - 데이터가 없는 경우")
    void givenUserIdAndPageable_whenGetUserCouponsAndHasNoData_thenReturnPageOfEmptyList() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<UserCouponGetResponseForMyPageQuerydsl> userCouponGetResponseForMyPageQuerydslPage =
                userCouponRepository.getUserCoupons(user.getId(), pageable);
        List<UserCouponGetResponseForMyPageQuerydsl> userCouponGetResponseForMyPageQuerydslList =
                userCouponGetResponseForMyPageQuerydslPage.getContent();

        assertThat(userCouponGetResponseForMyPageQuerydslPage).isNotNull();
        assertThat(userCouponGetResponseForMyPageQuerydslList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("적용 가능한 카테고리 쿠폰 조회")
    void givenUserIdAndBookId_whenGetUsableUserCategoryCouponsByBookId_thenReturnListOfUserCouponGetResponseForOrderQuerydsl() {
        userCouponRepository.save(userBookCoupon);
        userCouponRepository.save(userTotalCoupon);
        userCouponRepository.save(userCategoryCoupon);
        List<UserCouponGetResponseForOrderQuerydsl> userCouponGetResponseForOrderQuerydslList =
                userCouponRepository.getUsableUserCategoryCouponsByBookId(user.getId(), book.getId());

        assertThat(userCouponGetResponseForOrderQuerydslList).isNotNull().hasSize(1);
        assertThat(userCouponGetResponseForOrderQuerydslList.get(0).getUserCouponId()).isEqualTo(
                userCategoryCoupon.getId());
    }

    @Test
    @DisplayName("적용 가능한 도서 쿠폰 조회")
    void givenUserIdAndBookId_whenGetUsableUserBookCouponsByBookId_thenReturnListOfUserCouponGetResponseForOrderQuerydsl() {
        userCouponRepository.save(userBookCoupon);
        userCouponRepository.save(userTotalCoupon);
        userCouponRepository.save(userCategoryCoupon);
        List<UserCouponGetResponseForOrderQuerydsl> userCouponGetResponseForOrderQuerydslList =
                userCouponRepository.getUsableUserBookCouponsByBookId(user.getId(), book.getId());

        assertThat(userCouponGetResponseForOrderQuerydslList).isNotNull().hasSize(1);
        assertThat(userCouponGetResponseForOrderQuerydslList.get(0).getUserCouponId()).isEqualTo(
                userBookCoupon.getId());
    }

    @Test
    @DisplayName("적용 가능한 전체 쿠폰 조회")
    void givenUserId_whenGetUsableUserTotalCoupons_thenReturnListOfUserCouponGetResponseForOrderQuerydsl() {
        userCouponRepository.save(userBookCoupon);
        userCouponRepository.save(userTotalCoupon);
        userCouponRepository.save(userCategoryCoupon);
        List<UserCouponGetResponseForOrderQuerydsl> userCouponGetResponseForOrderQuerydslList =
                userCouponRepository.getUsableTotalCoupons(user.getId());

        assertThat(userCouponGetResponseForOrderQuerydslList).isNotNull().hasSize(1);
        assertThat(userCouponGetResponseForOrderQuerydslList.get(0).getUserCouponId()).isEqualTo(
                userTotalCoupon.getId());
    }

}