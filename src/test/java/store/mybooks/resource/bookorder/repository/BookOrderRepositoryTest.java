package store.mybooks.resource.bookorder.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.enumulation.DeliveryRuleNameEnum;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.ordersstatus.enumulation.OrdersStatusEnum;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.enumeration.UserStatusEnum;
import store.mybooks.resource.usercoupon.entity.UserCoupon;

/**
 * packageName    : store.mybooks.resource.bookorder.repository<br>
 * fileName       : BookOrderRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 3/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/26/24        minsu11       최초 생성<br>
 */
@DataJpaTest
class BookOrderRepositoryTest {
    @Autowired
    private BookOrderRepository bookOrderRepository;

    @Autowired
    TestEntityManager testEntityManager;

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

    private BookOrder bookOrder;

    private final int page = 0;
    private final int size = 2;

    private BookOrderUserResponse bookOrderUserResponse;
    private OrderDetailInfoResponse detailInfoResponse;

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


        bookOrder = BookOrder.builder()
                .id(1L)
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
                .build();
        bookOrderRepository.save(bookOrder);


        detailInfoResponse = new OrderDetailInfoResponse(1L, "test book",
                coupon.getId(), 10000, 1,
                false, "test image",
                OrdersStatusEnum.ORDERS_COMPLETE.toString(), 1L);
        bookOrderUserResponse = new BookOrderUserResponse(
                OrdersStatusEnum.ORDERS_COMPLETE.name(),
                DeliveryRuleNameEnum.DELIVERY_FEE.getValue(),
                deliveryRule.getCost(),
                bookOrder.getDate(),
                bookOrder.getInvoiceNumber(),
                bookOrder.getReceiverName(),
                bookOrder.getReceiverAddress(),
                bookOrder.getReceiverPhoneNumber(),
                bookOrder.getReceiverMessage(),
                bookOrder.getTotalCost(),
                bookOrder.getPointCost(),
                bookOrder.getCouponCost(),
                bookOrder.getNumber(),
                bookOrder.getId(),
                List.of(detailInfoResponse));


    }

    @Test
    @DisplayName("주문 번호로 주문 찾기")
    void givenOrderNumber_whenFindByNumber() {
        BookOrder actual = bookOrderRepository.findByNumber("testOrderNumber")
                .orElseThrow(BookOrderNotExistException::new);

        Assertions.assertEquals(bookOrder.getNumber(), actual.getNumber());
        Assertions.assertEquals(bookOrder.getOrderStatus(), actual.getOrderStatus());
        Assertions.assertEquals(bookOrder.getInvoiceNumber(), actual.getInvoiceNumber());
        Assertions.assertEquals(bookOrder.getUser(), actual.getUser());
        Assertions.assertEquals(bookOrder.getDate(), actual.getDate());
        Assertions.assertEquals(bookOrder.getReceiverAddress(), actual.getReceiverAddress());
        Assertions.assertEquals(bookOrder.getReceiverMessage(), actual.getReceiverMessage());
        Assertions.assertEquals(bookOrder.getReceiverName(), actual.getReceiverName());
        Assertions.assertEquals(bookOrder.getReceiverPhoneNumber(), actual.getReceiverPhoneNumber());
        Assertions.assertEquals(bookOrder.getIsCouponUsed(), actual.getIsCouponUsed());
        Assertions.assertEquals(bookOrder.getTotalCost(), actual.getTotalCost());
        Assertions.assertEquals(bookOrder.getPointCost(), actual.getPointCost());
        Assertions.assertEquals(bookOrder.getCouponCost(), actual.getCouponCost());
        assertThat(actual.getOutDate()).isNull();
        assertThat(actual.getInvoiceNumber()).isNull();
        assertThat(actual.getFindPassword()).isNull();
    }

    @Test
    @DisplayName("회원의 주문 내역 조회")
    void givenUserIdAndPageable_whenGetBookOrderPageByUserId_thenReturnBookOrderUserResponsePage() {
        Pageable pageable = PageRequest.of(page, size);
        List<BookOrderUserResponse> bookOrderUserResponses = List.of(bookOrderUserResponse);
        Page<BookOrderUserResponse> expected = new PageImpl<>(bookOrderUserResponses, pageable, bookOrderUserResponses.size());
        Page<BookOrderUserResponse> actual = bookOrderRepository.getBookOrderPageByUserId(user.getId(), pageable);
        Assertions.assertEquals(expected.getContent().size(), actual.getContent().size());
        Assertions.assertEquals(expected.getContent().get(0).getDeliveryRuleName(), actual.getContent().get(0).getDeliveryRuleName());
        Assertions.assertEquals(expected.getContent().get(0).getDeliveryCost(), actual.getContent().get(0).getDeliveryCost());
        Assertions.assertEquals(expected.getContent().get(0).getOrderDate(), actual.getContent().get(0).getOrderDate());
        Assertions.assertEquals(expected.getContent().get(0).getInvoiceNumber(), actual.getContent().get(0).getInvoiceNumber());
        Assertions.assertEquals(expected.getContent().get(0).getReceiverName(), actual.getContent().get(0).getReceiverName());
        Assertions.assertEquals(expected.getContent().get(0).getReceiverAddress(), actual.getContent().get(0).getReceiverAddress());
        Assertions.assertEquals(expected.getContent().get(0).getReceiverMessage(), actual.getContent().get(0).getReceiverMessage());
        Assertions.assertEquals(expected.getContent().get(0).getReceiverPhoneNumber(), actual.getContent().get(0).getReceiverPhoneNumber());
        Assertions.assertEquals(expected.getContent().get(0).getTotalCost(), actual.getContent().get(0).getTotalCost());
        Assertions.assertEquals(expected.getContent().get(0).getPointCost(), actual.getContent().get(0).getPointCost());
        Assertions.assertEquals(expected.getContent().get(0).getCouponCost(), actual.getContent().get(0).getCouponCost());
        Assertions.assertEquals(expected.getContent().get(0).getNumber(), actual.getContent().get(0).getNumber());
    }

    @Test
    @DisplayName("관리자 페이지에서 회원의 주문 내역 조회")
    void givenPageable_whenGetBookOrderPageByOrderStatusId_thenReturnBookOrderAdminResponsePage() {
        Pageable pageable = PageRequest.of(page, size);
        bookOrderRepository.getBookOrderPageByOrderStatusId(pageable);
    }

    @Test
    @DisplayName("주문 번호가 있는지 없는지 테스트(있는 경우)")
    void givenOrderNumber_whenExistBookOrderByOrderNumber_thenReturnTrue() {
        Assertions.assertTrue(bookOrderRepository.existBookOrderByOrderNumber("testOrderNumber"));
    }

    @Test
    @DisplayName("주문 번호가 있는지 없는지 테스트(없는 경우)")
    void givenOrderNumber_whenExistBookOrderByOrderNumber_thenReturnFalse() {
        Assertions.assertTrue(bookOrderRepository.existBookOrderByOrderNumber("testOrderNumber"));
    }

    @Test
    @DisplayName("주문번호가 있는 주문 조회")
    void givenOrderNumber_whenFindBookOrderInfo_thenReturnOptionalBookOrderInfoPayResponse() {
        List<OrderDetailInfoResponse> orderDetailInfoResponses = List.of(detailInfoResponse);
        bookOrderRepository.findBookOrderInfo("testOrderNumber");
    }
}