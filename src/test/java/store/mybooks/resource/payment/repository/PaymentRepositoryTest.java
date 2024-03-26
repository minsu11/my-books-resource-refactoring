package store.mybooks.resource.payment.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.enumulation.DeliveryRuleNameEnum;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.ordersstatus.enumulation.OrdersStatusEnum;
import store.mybooks.resource.payment.dto.response.PaymentResponse;
import store.mybooks.resource.payment.entity.Payment;
import store.mybooks.resource.payment.exception.PaymentNotExistException;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.enumeration.UserStatusEnum;
import store.mybooks.resource.usercoupon.entity.UserCoupon;

/**
 * packageName    : store.mybooks.resource.payment.repository<br>
 * fileName       : PaymentRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 3/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/26/24        minsu11       최초 생성<br>
 */
@DataJpaTest
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private Payment payment;
    private BookOrder bookOrder;
    private User user;

    private UserGrade userGrade;
    private UserGradeName userGradeName;

    private UserStatus userStatus;
    private Category category;

    private OrdersStatus ordersStatus;
    private DeliveryRule deliveryRule;
    private DeliveryRuleName deliveryRuleName;
    private UserCoupon userCoupon;
    private Coupon coupon;


    @BeforeEach
    void setUp() {

        userGradeName = testEntityManager.persist(
                new UserGradeName(UserGradeNameEnum.NORMAL.name())
        );
        userGrade = testEntityManager.persist(
                new UserGrade(
                        1000,
                        10000,
                        1,
                        userGradeName)
        );
        userStatus = testEntityManager.persist(new UserStatus(UserStatusEnum.ACTIVE.toString()));
        user = testEntityManager.persist(new User("test@test.com", 2024, "03-24", "1234", "010-1111-1111", false,
                "testUserName", userStatus, userGrade, "testOauthId"));
        ordersStatus = testEntityManager.persist(
                new OrdersStatus(OrdersStatusEnum.ORDERS_COMPLETE.toString())
        );

        deliveryRuleName = testEntityManager.persist(
                new DeliveryRuleName(DeliveryRuleNameEnum.DELIVERY_FEE.getValue())
        );

        deliveryRule = testEntityManager.persist(new DeliveryRule(
                deliveryRuleName,
                "cj 택배",
                5000,
                10000)
        );

        category = testEntityManager.persist(
                new Category(
                        null,
                        "test category"
                )
        );

        coupon = testEntityManager.persist(
                new Coupon("test coupon", null, category,
                        0, 1000,
                        null, null,
                        LocalDate.of(2024, 3, 1),
                        LocalDate.of(2030, 3, 20),
                        false, false)
        );

        userCoupon = testEntityManager.persist(
                new UserCoupon(user, coupon)
        );


        bookOrder = testEntityManager.persist(BookOrder.builder()
                .deliveryDate(LocalDate.of(2024, 3, 5))
                .date(LocalDate.of(2024, 3, 3))
                .receiverName("이순신")
                .receiverAddress("거북선")
                .receiverPhoneNumber("010-1234-1234")
                .receiverMessage("광화문 앞에 두세요")
                .totalCost(99000)
                .pointCost(1000)
                .couponCost(0)
                .isCouponUsed(false)
                .number("testOrderNumber")
                .user(user)
                .orderStatus(ordersStatus)
                .deliveryRule(deliveryRule)
                .userCoupon(userCoupon)
                .build()
        );
        payment = Payment
                .builder()
                .createdAt(LocalDateTime.now())
                .orderNumber("toss payment key")
                .status("DONE")
                .buyer(bookOrder.getUser().getName())
                .cost(bookOrder.getTotalCost())
                .bookOrder(bookOrder)
                .build();
        paymentRepository.save(payment);
    }

    @Test
    @DisplayName("토스 페이먼츠에서 준 payment key로 결제 조회")
    void givenOrderNumber_whenFindByOrderNumber_thenReturnPayment() {
        Payment expected = payment;
        Payment actual = paymentRepository.findByOrderNumber("testOrderNumber")
                .orElseThrow(PaymentNotExistException::new);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("주문 번호로 된 결제 내역이 유무 테스트")
    void givenBookOrderNumber_whenExistPaymentByOrderNumber_thenReturnTrue() {
        Assertions.assertTrue(paymentRepository.existPaymentByOrderNumber("testOrderNumber"));
    }


    @Test
    @DisplayName("주문 번호로 된 결제 내역이 유무 테스트")
    void givenBookOrderNumber_whenExistPaymentByOrderNumber_thenReturnFalse() {
        Assertions.assertFalse(paymentRepository.existPaymentByOrderNumber("testOrderNumber123"));
    }

    @Test
    @DisplayName("주문 번호로 결제 내역의 토스페이먼츠 조회")
    void givenOrderNumber_whenGetPaymentKey_thenReturnPaymentResponse() {
        PaymentResponse expected = new PaymentResponse("toss payment key");
        PaymentResponse actual = paymentRepository.getPaymentKey("testOrderNumber")
                .orElseThrow(PaymentNotExistException::new);
        Assertions.assertEquals(expected.getPaymentKey(), actual.getPaymentKey());
    }

}