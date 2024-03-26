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
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderPaymentInfoRespones;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.enumulation.DeliveryRuleNameEnum;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.orderdetail.entity.OrderDetail;
import store.mybooks.resource.orderdetailstatus.entity.OrderDetailStatus;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.ordersstatus.enumulation.OrdersStatusEnum;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.enumeration.UserStatusEnum;
import store.mybooks.resource.usercoupon.entity.UserCoupon;
import store.mybooks.resource.utils.TimeUtils;
import store.mybooks.resource.wrap.entity.Wrap;

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

    private OrdersStatus ordersStatus1;
    private OrdersStatus ordersStatus2;
    private DeliveryRule deliveryRule;
    private DeliveryRuleName deliveryRuleName;
    private UserCoupon userCoupon;
    private Coupon coupon;

    private BookOrder bookOrder1;
    private BookOrder bookOrder2;

    private OrderDetail orderDetail1;
    private OrderDetail orderDetail2;

    private Wrap wrap;
    private OrderDetailStatus orderDetailStatus;

    private Book book1;
    private Book book2;
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
        ordersStatus1 = testEntityManager.persist(
                new OrdersStatus(OrdersStatusEnum.WAIT.toString())
        );
        ordersStatus2 = testEntityManager.persist(
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


        bookOrder1 = BookOrder.builder()
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
                .orderStatus(ordersStatus1)
                .deliveryRule(deliveryRule)
                .userCoupon(userCoupon)
                .build();

        bookOrderRepository.save(bookOrder1);

        bookOrder2 = BookOrder.builder()
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
                .number("testOrderNumber2")
                .user(user)
                .orderStatus(ordersStatus2)
                .deliveryRule(deliveryRule)
                .userCoupon(userCoupon)
                .build();
        bookOrderRepository.save(bookOrder2);

        orderDetailStatus = testEntityManager.persist(new OrderDetailStatus(
                OrdersStatusEnum.WAIT.toString()
        ));

        wrap = testEntityManager.persist(new Wrap());

        Publisher publisher = new Publisher("출판사명");
        testEntityManager.persist(publisher);

        BookStatus bookStatus = new BookStatus("상태명");
        testEntityManager.persist(bookStatus);

        book1 = testEntityManager.persist(Book.builder()
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
                .build()
        );

        book2 = testEntityManager.persist(Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name("도서명1")
                .isbn("1234567890122")
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
                .build()
        );

        orderDetail1 = testEntityManager.persist(
                OrderDetail
                        .builder()
                        .amount(1)
                        .book(book1)
                        .bookCost(book1.getSaleCost())
                        .bookOrder(bookOrder1)
                        .createDate(bookOrder1.getDate())
                        .detailStatus(orderDetailStatus)
                        .isCouponUsed(false)
                        .userCoupon(userCoupon)
                        .wrap(wrap)
                        .build()
        );

        orderDetail2 = testEntityManager.persist(
                OrderDetail
                        .builder()
                        .amount(1)
                        .book(book2)
                        .bookCost(book2.getSaleCost())
                        .bookOrder(bookOrder1)
                        .createDate(LocalDate.of(2024, 3, 25))
                        .detailStatus(orderDetailStatus)
                        .isCouponUsed(false)
                        .userCoupon(userCoupon)
                        .wrap(wrap)
                        .build()
        );

        detailInfoResponse = new OrderDetailInfoResponse(1L, "test book",
                coupon.getId(), 10000, 1,
                false, "test image",
                OrdersStatusEnum.ORDERS_COMPLETE.toString(), 1L);
        bookOrderUserResponse = new BookOrderUserResponse(
                OrdersStatusEnum.ORDERS_COMPLETE.name(),
                DeliveryRuleNameEnum.DELIVERY_FEE.getValue(),
                deliveryRule.getCost(),
                bookOrder2.getDate(),
                bookOrder2.getInvoiceNumber(),
                bookOrder2.getReceiverName(),
                bookOrder2.getReceiverAddress(),
                bookOrder2.getReceiverPhoneNumber(),
                bookOrder2.getReceiverMessage(),
                bookOrder2.getTotalCost(),
                bookOrder2.getPointCost(),
                bookOrder2.getCouponCost(),
                bookOrder2.getNumber(),
                bookOrder2.getId(),
                List.of(detailInfoResponse));


    }

    @Test
    @DisplayName("주문 번호로 주문 찾기")
    void givenOrderNumber_whenFindByNumber() {
        BookOrder actual = bookOrderRepository.findByNumber("testOrderNumber")
                .orElseThrow(BookOrderNotExistException::new);

        Assertions.assertEquals(bookOrder1.getNumber(), actual.getNumber());
        Assertions.assertEquals(bookOrder1.getOrderStatus(), actual.getOrderStatus());
        Assertions.assertEquals(bookOrder1.getInvoiceNumber(), actual.getInvoiceNumber());
        Assertions.assertEquals(bookOrder1.getUser(), actual.getUser());
        Assertions.assertEquals(bookOrder1.getDate(), actual.getDate());
        Assertions.assertEquals(bookOrder1.getReceiverAddress(), actual.getReceiverAddress());
        Assertions.assertEquals(bookOrder1.getReceiverMessage(), actual.getReceiverMessage());
        Assertions.assertEquals(bookOrder1.getReceiverName(), actual.getReceiverName());
        Assertions.assertEquals(bookOrder1.getReceiverPhoneNumber(), actual.getReceiverPhoneNumber());
        Assertions.assertEquals(bookOrder1.getIsCouponUsed(), actual.getIsCouponUsed());
        Assertions.assertEquals(bookOrder1.getTotalCost(), actual.getTotalCost());
        Assertions.assertEquals(bookOrder1.getPointCost(), actual.getPointCost());
        Assertions.assertEquals(bookOrder1.getCouponCost(), actual.getCouponCost());
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
        BookOrderAdminResponse bookOrderAdminResponse = new BookOrderAdminResponse(1L, bookOrder1.getId(), bookOrder1.getOrderStatus().getId(),
                bookOrder1.getDate(), LocalDate.of(2024, 3, 25), bookOrder1.getInvoiceNumber(), bookOrder1.getNumber());

        List<BookOrderAdminResponse> orderAdminResponses = List.of(bookOrderAdminResponse);
        Page<BookOrderAdminResponse> expected = new PageImpl<>(orderAdminResponses, pageable, orderAdminResponses.size());
        Page<BookOrderAdminResponse> actual = bookOrderRepository.getBookOrderPageByOrderStatusId(pageable);
        Assertions.assertEquals(expected.getContent().get(0).getNumber(), actual.getContent().get(0).getNumber());
        Assertions.assertEquals(expected.getContent().get(0).getInvoiceNumber(), actual.getContent().get(0).getInvoiceNumber());
        Assertions.assertEquals(expected.getContent().get(0).getDate(), actual.getContent().get(0).getDate());
        Assertions.assertEquals(expected.getContent().get(0).getStatusId(), actual.getContent().get(0).getStatusId());


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
        BookOrderInfoPayResponse expected = new BookOrderInfoPayResponse(OrdersStatusEnum.WAIT.toString(),
                bookOrder1.getNumber(), bookOrder1.getTotalCost(), bookOrder1.getIsCouponUsed(), bookOrder1.getPointCost()
        );
        BookOrderInfoPayResponse actual = bookOrderRepository.findBookOrderInfo("testOrderNumber")
                .orElseThrow(BookOrderNotExistException::new);
        Assertions.assertEquals(expected.getNumber(), actual.getNumber());
        Assertions.assertEquals(expected.getOrderStatus(), actual.getOrderStatus());
        Assertions.assertEquals(expected.getTotalCost(), actual.getTotalCost());
        Assertions.assertEquals(expected.getPointCost(), actual.getPointCost());
        Assertions.assertEquals(expected.getIsCouponUsed(), actual.getIsCouponUsed());
    }

    @Test
    @DisplayName("주문 할 때 주문 번호로 결제 정보 조회")
    void givenOrderNumber_whenFindOrderPayInfo_thenReturnBookOrderPaymentInfoResponse() {
        BookOrderPaymentInfoRespones expected = new BookOrderPaymentInfoRespones(
                user.getName(), "test@test.com", user.getPhoneNumber(),
                bookOrder1.getNumber(), ordersStatus1.getId()
        );

        BookOrderPaymentInfoRespones actual = bookOrderRepository.findOrderPayInfo("testOrderNumber")
                .orElseThrow(BookOrderNotExistException::new);

        Assertions.assertEquals(expected.getOrderNumber(), actual.getOrderNumber());
        Assertions.assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getOrderStatus(), actual.getOrderStatus());
    }

    @Test
    @DisplayName("회원의 주문의 건 수 계산")
    void givenUserId_whenGetUserBookOrderCount_thenReturnLong() {
        Assertions.assertEquals(2L, bookOrderRepository.getUserBookOrderCount(user.getId()));
    }


}