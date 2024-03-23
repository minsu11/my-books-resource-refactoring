package store.mybooks.resource.pointhistory.repository;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.orderdetail.entity.OrderDetail;
import store.mybooks.resource.orderdetailstatus.entity.OrderDetailStatus;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.entity.PointHistory;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_address.entity.UserAddress;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.pointhistory.repository
 * fileName       : PointHistoryRepositoryTest
 * author         : damho-lee
 * date           : 3/23/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/23/24          damho-lee          최초 생성
 */
@DataJpaTest
class PointHistoryRepositoryTest {
    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    TestEntityManager testEntityManager;

    UserStatus userStatus;

    UserGradeName userGradeName;

    UserGrade userGrade;

    User user;

    UserAddress userAddress;

    BookStatus bookStatus;

    Publisher publisher;

    Book book;

    OrderDetailStatus orderDetailStatus;

    DeliveryRuleName deliveryRuleName;

    DeliveryRule deliveryRule;

    BookOrder bookOrder;

    OrdersStatus ordersStatus;

    OrderDetail orderDetail;

    PointRuleName login;

    PointRuleName signUp;

    PointRuleName use;

    PointRule loginPoint;

    PointRule signupPoint;

    PointRule usePoint;

    PointHistory signupPointHistory;

    PointHistory loginPointHistory;

    PointHistory usePointHistory;

    @BeforeEach
    void setup() {
        userStatus = new UserStatus("활동중");
        testEntityManager.persist(userStatus);

        userGradeName = new UserGradeName("일반");
        testEntityManager.persist(userGradeName);

        userGrade = new UserGrade(0, 100000, 2, LocalDate.now(), userGradeName);
        testEntityManager.persist(userGrade);

        user = new User(
                "test@naver.com",
                2024,
                "03-24",
                "dummy",
                "01012341234",
                false,
                "테스트유저",
                userStatus,
                userGrade
        );
        testEntityManager.persist(user);

        userAddress = new UserAddress(user, "집", "광주광역시 동구 조선대6길 34 (서석동)", "726A호", 61452, null);
        testEntityManager.persist(userAddress);

        bookStatus = new BookStatus("판매중");
        testEntityManager.persist(bookStatus);

        publisher = new Publisher("조선 출판사");
        testEntityManager.persist(publisher);

        book = Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name("아프니까 청춘이다")
                .isbn("9788965700180")
                .publishDate(LocalDate.of(2010, 12, 24))
                .page(320)
                .index("index")
                .explanation("아프니까 청춘입니다.")
                .originalCost(10000)
                .discountRate(20)
                .saleCost(8000)
                .stock(10)
                .viewCount(0)
                .isPackaging(true)
                .createdDate(LocalDate.of(2010, 8, 1))
                .build();
        testEntityManager.persist(book);

        orderDetailStatus = new OrderDetailStatus("구매 확정");
        testEntityManager.persist(ordersStatus);

        deliveryRuleName = new DeliveryRuleName("배송비");
        testEntityManager.persist(deliveryRuleName);

        deliveryRule = new DeliveryRule(deliveryRuleName, "CJ대한통운", 3000, 10000);
        testEntityManager.persist(deliveryRule);

        bookOrder = new BookOrder(
                null,
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 3),
                LocalDate.of(2024, 2, 28),
                "912871535",
                "김철수",
                userAddress.getRoadName().concat(userAddress.getDetail()),
                "01012341234",
                null,
                6000,
                2000,
                0,
                false,
                "a98ewg9r7",
                null,
                user,
                ordersStatus,
                deliveryRule,
                null
        );
        testEntityManager.persist(bookOrder);

        orderDetail = OrderDetail.builder()
                .bookCost(8000)
                .amount(1)
                .isCouponUsed(false)
                .bookOrder(bookOrder)
                .book(book)
                .detailStatus(orderDetailStatus)
                .userCoupon(null)
                .createDate(bookOrder.getDate())
                .build();
        testEntityManager.persist(orderDetail);

        login = new PointRuleName("로그인 적립");
        testEntityManager.persist(login);

        signUp = new PointRuleName("회원가입 적립");
        testEntityManager.persist(signUp);

        use = new PointRuleName("포인트 사용");

        loginPoint = new PointRule(
                login,
                null,
                500
        );
        testEntityManager.persist(loginPoint);

        signupPoint = new PointRule(
                signUp,
                null,
                2000
        );
        testEntityManager.persist(loginPoint);

        usePoint = new PointRule(
                use,
                null,
                null
        );

        signupPointHistory = new PointHistory(
                loginPoint.getCost(),
                user,
                signupPoint,
                null
        );

        loginPointHistory = new PointHistory(
                loginPoint.getCost(),
                user,
                loginPoint,
                null
        );

        usePointHistory = new PointHistory(
                -2000,
                user,
                usePoint,
                bookOrder
        );
    }

    @Test
    @DisplayName("getRemainingPoint 테스트")
    void givenUserId_whenGetRemainingPoint_thenReturnPointResponse() {
        PointResponse pointResponse = pointHistoryRepository.getRemainingPoint(user.getId());
    }

    @Test
    @DisplayName("getPointHistoryByUserId 테스트")
    void givenPageableAndUserId_whenGetPointHistoryByUserId_thenReturnPageOfPointHistoryResponse() {
    }
}