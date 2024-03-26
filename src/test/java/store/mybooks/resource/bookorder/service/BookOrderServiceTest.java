package store.mybooks.resource.bookorder.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
import store.mybooks.resource.bookorder.dto.mapper.BookOrderMapper;
import store.mybooks.resource.bookorder.dto.request.BookInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderAdminModifyRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderRegisterInvoiceRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderPaymentInfoRespones;
import store.mybooks.resource.bookorder.dto.response.BookOrderRegisterInvoiceResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminModifyResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.bookorder.exception.BookOrderInfoNotMatchException;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotExistsException;
import store.mybooks.resource.delivery_rule.repository.DeliveryRuleRepository;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailCreateResponse;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.orderdetail.repository.OrderDetailRepository;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.ordersstatus.exception.OrdersStatusNotExistException;
import store.mybooks.resource.ordersstatus.repository.OrdersStatusRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_address.repository.UserAddressRepository;

/**
 * packageName    : store.mybooks.resource.bookorder.service<br>
 * fileName       : BookOrderServiceTest<br>
 * author         : minsu11<br>
 * date           : 3/25/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/25/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
class BookOrderServiceTest {
    @InjectMocks
    private BookOrderService bookOrderService;

    @Mock
    private BookOrderRepository bookOrderRepository;

    @Mock
    private OrdersStatusRepository ordersStatusRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private BookOrderMapper mapper;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private DeliveryRuleRepository deliveryRuleRepository;

    @Mock
    private UserRepository userRepository;

    private final int size = 2;
    private final int page = 0;

    @Test
    @DisplayName("회원 아이디로 주문 내역 목록 페이징 조회")
    void givenUserIdAndPageable_whenGetBookOrderPageByUserId_thenReturnBookOrderUserResponsePage() {
        List<OrderDetailInfoResponse> orderDetailInfoResponses =
                List.of(new OrderDetailInfoResponse(1L, "test book", 1L, 2000, 2000, true,
                        "test image", "test status", 1L));

        List<BookOrderUserResponse> responses =
                List.of(new BookOrderUserResponse("test", "testRuleName", 100,
                        LocalDate.of(1212, 12, 12), "testInvoiceNumber",
                        "testName", "testAddress", "010-1111-1111", "testMessage", 1000, 100,
                        100, "test number", 1L, orderDetailInfoResponses));
        Pageable pageable = PageRequest.of(page, size);

        Page<BookOrderUserResponse> expected = new PageImpl<>(responses, pageable, responses.size());

        given(bookOrderRepository.getBookOrderPageByUserId(any(), any())).willReturn(expected);

        Page<BookOrderUserResponse> actual = bookOrderService.getBookOrderResponseList(1L, pageable);

        Assertions.assertEquals(expected, actual);
        verify(bookOrderRepository, times(1)).getBookOrderPageByUserId(any(), any());
    }

    @Test
    @DisplayName("전체 주문 내역 페이징 조회")
    void givenPageable_whenGetBookOrderPageByOrderStatusId_thenReturnBookOrderAdminResponse() {
        BookOrderAdminResponse bookOrderAdminResponse = new BookOrderAdminResponse(
                1L, 1L, "test", LocalDate.of(12212, 12, 12),
                LocalDate.of(1212, 12, 12), "test", "test"
        );
        List<BookOrderAdminResponse> list = List.of(bookOrderAdminResponse);

        Pageable pageable = PageRequest.of(page, size);

        Page<BookOrderAdminResponse> expected = new PageImpl<>(list, pageable, size);
        given(bookOrderRepository.getBookOrderPageByOrderStatusId(pageable)).willReturn(expected);

        Page<BookOrderAdminResponse> actual = bookOrderService.getBookOrderAdminResponseList(pageable);

        Assertions.assertEquals(expected, actual);

        verify(bookOrderRepository, times(1)).getBookOrderPageByOrderStatusId(any());
    }

    @Test
    @DisplayName("관리자가 주문 상태를 변경 성공 테스트")
    void givenBookOrderAdminModifyRequest_whenModifyBookOrderAdmin_thenReturnBookOrderAdminModifyResponse(
            @Mock OrdersStatus ordersStatus, @Mock BookOrder bookOrder
    ) {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "testtest");

        BookOrderAdminModifyResponse expected = new BookOrderAdminModifyResponse(1L, "test", LocalDate.of(1212, 12, 12));
        given(ordersStatusRepository.findById(anyString())).willReturn(Optional.of(ordersStatus));
        given(bookOrderRepository.findById(anyLong())).willReturn(Optional.of(bookOrder));
        given(mapper.mapToBookOrderModifyOrderStatusResponse(any())).willReturn(expected);

        BookOrderAdminModifyResponse actual = bookOrderService.modifyBookOrderAdminStatus(request);
        Assertions.assertEquals(expected, actual);
        verify(ordersStatusRepository, times(1)).findById(anyString());
        verify(bookOrderRepository, times(1)).findById(anyLong());
        verify(mapper, times(1)).mapToBookOrderModifyOrderStatusResponse(any());
    }

    @Test
    @DisplayName("관리자가 주문 상태 변경 실패 테스트(주문 상태가 없는 경우)")
    void givenBookOrderAdminModifyRequest_whenFindById_thenThrowOrdersStatusNotExistException() {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "testtest");
        given(ordersStatusRepository.findById(anyString())).willThrow(OrdersStatusNotExistException.class);
        Assertions.assertThrows(OrdersStatusNotExistException.class, () -> bookOrderService.modifyBookOrderAdminStatus(request));
        verify(ordersStatusRepository, times(1)).findById(anyString());
        verify(bookOrderRepository, never()).findById(anyLong());
        verify(mapper, never()).mapToBookOrderModifyOrderStatusResponse(any());
    }

    @Test
    @DisplayName("관리자가 주문 상태 변경 실패 테스트(주문 정보가 없는 경우)")
    void givenBookOrderAdminModifyRequest_whenFindById_thenThrowBookOrderNotExistException(
            @Mock OrdersStatus ordersStatus
    ) {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "testtest");
        given(ordersStatusRepository.findById(anyString())).willReturn(Optional.of(ordersStatus));
        given(bookOrderRepository.findById(anyLong())).willThrow(BookOrderNotExistException.class);
        Assertions.assertThrows(BookOrderNotExistException.class, () -> bookOrderService.modifyBookOrderAdminStatus(request));
        verify(ordersStatusRepository, times(1)).findById(anyString());
        verify(bookOrderRepository, times(1)).findById(anyLong());
        verify(mapper, never()).mapToBookOrderModifyOrderStatusResponse(any());
    }

    @Test
    @DisplayName("관리자가 송장 번호 성공 테스트")
    void givenBookOrderRegisterInvoiceRequest_whenRegisterBookOrderInvoiceNumber_thenReturnBookOrderRegisterInvoiceResponse(
            @Mock BookOrder bookOrder
    ) {
        BookOrderRegisterInvoiceRequest request = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "testtest");
        BookOrderRegisterInvoiceResponse expected = new BookOrderRegisterInvoiceResponse("testtest");
        given(bookOrderRepository.findById(anyLong())).willReturn(Optional.of(bookOrder));
        given(mapper.mapToBookOrderRegisterInvoiceResponse(any())).willReturn(expected);

        BookOrderRegisterInvoiceResponse actual = bookOrderService.registerBookOrderInvoiceNumber(request);
        Assertions.assertEquals(expected, actual);
        verify(bookOrderRepository, times(1)).findById(anyLong());
        verify(mapper, times(1)).mapToBookOrderRegisterInvoiceResponse(any());
    }

    @Test
    @DisplayName("관리자가 송장 번호 실패 테스트")
    void givenBookOrderRegisterInvoiceRequest_whenRegisterBookOrderInvoiceNumber_thenThrowBookOrderNotExistException(
    ) {
        BookOrderRegisterInvoiceRequest request = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "testtest");
        given(bookOrderRepository.findById(anyLong())).willThrow(BookOrderNotExistException.class);

        Assertions.assertThrows(BookOrderNotExistException.class, () -> bookOrderService.registerBookOrderInvoiceNumber(request));
        verify(bookOrderRepository, times(1)).findById(anyLong());
        verify(mapper, never()).mapToBookOrderRegisterInvoiceResponse(any());
    }

    @Test
    @DisplayName("회원 주소가 맞는 경우 테스트")
    void givenAddressId_whenExistById_thenReturnTrue() {
        given(userAddressRepository.existsById(anyLong())).willReturn(true);

        Assertions.assertTrue(bookOrderService.checkUserOrderAddress(1L));
        verify(userAddressRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("회원 주소가 아닌 경우 테스트")
    void givenAddressId_whenExistById_thenThrowBookOrderInfoNotMatchException() {
        given(userAddressRepository.existsById(anyLong())).willReturn(false);
        Assertions.assertThrows(BookOrderInfoNotMatchException.class, () -> bookOrderService.checkUserOrderAddress(1L));
        verify(userAddressRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("주문지 생성 성공 테스트")
    void givenBookOrderCreateRequest_whenCreateBookOrder_thenReturnBookOrderCreateResponse(
            @Mock DeliveryRule deliveryRule, @Mock OrdersStatus ordersStatus,
            @Mock User user, @Mock BookOrder bookOrder
    ) {
        BookOrderInfoRequest infoRequest = new BookOrderInfoRequest();
        ReflectionTestUtils.setField(infoRequest, "deliveryId", 1);
        ReflectionTestUtils.setField(infoRequest, "deliveryDate", LocalDate.of(1212, 12, 12));
        ReflectionTestUtils.setField(infoRequest, "recipientName", "test");
        ReflectionTestUtils.setField(infoRequest, "recipientAddress", "test address");
        ReflectionTestUtils.setField(infoRequest, "recipientPhoneNumber", "010-1111-1111");
        ReflectionTestUtils.setField(infoRequest, "receiverMessage", "test message");
        ReflectionTestUtils.setField(infoRequest, "usingPoint", 100);
        ReflectionTestUtils.setField(infoRequest, "wrapCost", 10);
        ReflectionTestUtils.setField(infoRequest, "couponApplicationAmount", 1);

        BookOrderCreateRequest request = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "phone", "010-1111-1111");
        ReflectionTestUtils.setField(request, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(request, "orderInfo", infoRequest);
        ReflectionTestUtils.setField(request, "orderNumber", "test");
        ReflectionTestUtils.setField(request, "pointCost", 100);
        ReflectionTestUtils.setField(request, "couponCost", 1000);
        ReflectionTestUtils.setField(request, "totalCost", 100000);
        ReflectionTestUtils.setField(request, "wrapCost", 100);
        ReflectionTestUtils.setField(request, "orderCode", "etst");
        BookOrderCreateResponse expected = new BookOrderCreateResponse("test", "testnumber", 1000, false);
        given(deliveryRuleRepository.findById(any())).willReturn(Optional.of(deliveryRule));
        given(ordersStatusRepository.findById(any())).willReturn(Optional.of(ordersStatus));
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(mapper.mapToBookOrderCreateResponse(any())).willReturn(expected);

        BookOrderCreateResponse actual = bookOrderService.createBookOrder(request, 1L);
        Assertions.assertEquals(expected, actual);

        verify(deliveryRuleRepository, times(1)).findById(any());
        verify(ordersStatusRepository, times(1)).findById(any());
        verify(userRepository, times(1)).findById(anyLong());
        verify(mapper, times(1)).mapToBookOrderCreateResponse(any());

    }


    @Test
    @DisplayName("주문지 생성 실패 테스트(배송 규정이 없는 경우")
    void givenBookOrderCreateRequest_whenCreateBookOrder_thenThrowDeliveryRuleNotExistException(
    ) {
        BookOrderInfoRequest infoRequest = new BookOrderInfoRequest();
        ReflectionTestUtils.setField(infoRequest, "deliveryId", 1);
        ReflectionTestUtils.setField(infoRequest, "deliveryDate", LocalDate.of(1212, 12, 12));
        ReflectionTestUtils.setField(infoRequest, "recipientName", "test");
        ReflectionTestUtils.setField(infoRequest, "recipientAddress", "test address");
        ReflectionTestUtils.setField(infoRequest, "recipientPhoneNumber", "010-1111-1111");
        ReflectionTestUtils.setField(infoRequest, "receiverMessage", "test message");
        ReflectionTestUtils.setField(infoRequest, "usingPoint", 100);
        ReflectionTestUtils.setField(infoRequest, "wrapCost", 10);
        ReflectionTestUtils.setField(infoRequest, "couponApplicationAmount", 1);

        BookOrderCreateRequest request = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "phone", "010-1111-1111");
        ReflectionTestUtils.setField(request, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(request, "orderInfo", infoRequest);
        ReflectionTestUtils.setField(request, "orderNumber", "test");
        ReflectionTestUtils.setField(request, "pointCost", 100);
        ReflectionTestUtils.setField(request, "couponCost", 1000);
        ReflectionTestUtils.setField(request, "totalCost", 100000);
        ReflectionTestUtils.setField(request, "wrapCost", 100);
        ReflectionTestUtils.setField(request, "orderCode", "etst");
        BookOrderCreateResponse expected = new BookOrderCreateResponse("test", "testnumber", 1000, false);
        given(deliveryRuleRepository.findById(any())).willThrow(DeliveryRuleNotExistsException.class);

        Assertions.assertThrows(DeliveryRuleNotExistsException.class, () -> bookOrderService.createBookOrder(request, 1L));

        verify(deliveryRuleRepository, times(1)).findById(any());
        verify(ordersStatusRepository, never()).findById(any());
        verify(userRepository, never()).findById(anyLong());
        verify(mapper, never()).mapToBookOrderCreateResponse(any());
    }

    @Test
    @DisplayName("주문지 생성 실패 테스트(주문 상태가 없는 경우")
    void givenBookOrderCreateRequest_whenCreateBookOrder_thenThrowOrdersSatusNotExistException(@Mock DeliveryRule deliveryRule) {
        BookOrderInfoRequest infoRequest = new BookOrderInfoRequest();
        ReflectionTestUtils.setField(infoRequest, "deliveryId", 1);
        ReflectionTestUtils.setField(infoRequest, "deliveryDate", LocalDate.of(1212, 12, 12));
        ReflectionTestUtils.setField(infoRequest, "recipientName", "test");
        ReflectionTestUtils.setField(infoRequest, "recipientAddress", "test address");
        ReflectionTestUtils.setField(infoRequest, "recipientPhoneNumber", "010-1111-1111");
        ReflectionTestUtils.setField(infoRequest, "receiverMessage", "test message");
        ReflectionTestUtils.setField(infoRequest, "usingPoint", 100);
        ReflectionTestUtils.setField(infoRequest, "wrapCost", 10);
        ReflectionTestUtils.setField(infoRequest, "couponApplicationAmount", 1);

        BookOrderCreateRequest request = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "phone", "010-1111-1111");
        ReflectionTestUtils.setField(request, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(request, "orderInfo", infoRequest);
        ReflectionTestUtils.setField(request, "orderNumber", "test");
        ReflectionTestUtils.setField(request, "pointCost", 100);
        ReflectionTestUtils.setField(request, "couponCost", 1000);
        ReflectionTestUtils.setField(request, "totalCost", 100000);
        ReflectionTestUtils.setField(request, "wrapCost", 100);
        ReflectionTestUtils.setField(request, "orderCode", "etst");
        BookOrderCreateResponse expected = new BookOrderCreateResponse("test", "testnumber", 1000, false);
        given(deliveryRuleRepository.findById(any())).willReturn(Optional.of(deliveryRule));
        given(ordersStatusRepository.findById(any())).willThrow(OrdersStatusNotExistException.class);

        Assertions.assertThrows(OrdersStatusNotExistException.class, () -> bookOrderService.createBookOrder(request, 1L));

        verify(deliveryRuleRepository, times(1)).findById(any());
        verify(ordersStatusRepository, times(1)).findById(any());
        verify(userRepository, never()).findById(anyLong());
        verify(mapper, never()).mapToBookOrderCreateResponse(any());
    }

    @Test
    @DisplayName("주문지 생성 실패 테스트(유저가 없는 경우")
    void givenBookOrderCreateRequest_whenCreateBookOrder_thenThrowUserNotExistException(
            @Mock DeliveryRule deliveryRule, @Mock OrdersStatus ordersStatus) {
        BookOrderInfoRequest infoRequest = new BookOrderInfoRequest();
        ReflectionTestUtils.setField(infoRequest, "deliveryId", 1);
        ReflectionTestUtils.setField(infoRequest, "deliveryDate", LocalDate.of(1212, 12, 12));
        ReflectionTestUtils.setField(infoRequest, "recipientName", "test");
        ReflectionTestUtils.setField(infoRequest, "recipientAddress", "test address");
        ReflectionTestUtils.setField(infoRequest, "recipientPhoneNumber", "010-1111-1111");
        ReflectionTestUtils.setField(infoRequest, "receiverMessage", "test message");
        ReflectionTestUtils.setField(infoRequest, "usingPoint", 100);
        ReflectionTestUtils.setField(infoRequest, "wrapCost", 10);
        ReflectionTestUtils.setField(infoRequest, "couponApplicationAmount", 1);

        BookOrderCreateRequest request = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "phone", "010-1111-1111");
        ReflectionTestUtils.setField(request, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(request, "orderInfo", infoRequest);
        ReflectionTestUtils.setField(request, "orderNumber", "test");
        ReflectionTestUtils.setField(request, "pointCost", 100);
        ReflectionTestUtils.setField(request, "couponCost", 1000);
        ReflectionTestUtils.setField(request, "totalCost", 100000);
        ReflectionTestUtils.setField(request, "wrapCost", 100);
        ReflectionTestUtils.setField(request, "orderCode", "etst");
        BookOrderCreateResponse expected = new BookOrderCreateResponse("test", "testnumber", 1000, false);
        given(deliveryRuleRepository.findById(any())).willReturn(Optional.of(deliveryRule));
        given(ordersStatusRepository.findById(any())).willReturn(Optional.of(ordersStatus));
        given(userRepository.findById(any())).willThrow(UserNotExistException.class);


        Assertions.assertThrows(UserNotExistException.class, () -> bookOrderService.createBookOrder(request, 1L));

        verify(deliveryRuleRepository, times(1)).findById(any());
        verify(ordersStatusRepository, times(1)).findById(any());
        verify(userRepository, times(1)).findById(anyLong());
        verify(mapper, never()).mapToBookOrderCreateResponse(any());
    }

    @Test
    @DisplayName("주문 번호가 있는 경우 테스트")
    void givenOrderNumber_whenExistBookOrderByOrderNumber_thenReturnTrue() {
        given(bookOrderRepository.existBookOrderByOrderNumber(anyString())).willReturn(true);
        Boolean actual = bookOrderService.checkBookOrderNumberExists("test");
        Assertions.assertTrue(actual);
        verify(bookOrderRepository, times(1)).existBookOrderByOrderNumber(anyString());
    }

    @Test
    @DisplayName("주문 번호가 없는 경우 테스트")
    void givenOrderNumber_whenExistBookOrderByOrderNumber_thenReturnFalse() {
        given(bookOrderRepository.existBookOrderByOrderNumber(anyString())).willReturn(false);
        Boolean actual = bookOrderService.checkBookOrderNumberExists("test");
        Assertions.assertFalse(actual);
        verify(bookOrderRepository, times(1)).existBookOrderByOrderNumber(anyString());
    }

    @Test
    @DisplayName("주문 번호로 주문 조회 성공 테스트")
    void givenOrderNumber_whenGetBookInfo_thenReturnBookOrderInfoPayResponse() {
        BookOrderInfoPayResponse expected = new BookOrderInfoPayResponse();
        given(bookOrderRepository.findBookOrderInfo(anyString())).willReturn(Optional.of(expected));
        BookOrderInfoPayResponse actual = bookOrderService.getBookInfo("test");

        Assertions.assertEquals(expected, actual);
        verify(bookOrderRepository, times(1)).findBookOrderInfo(anyString());
    }

    @Test
    @DisplayName("주문 번호로 주문 조회 실패 테스트")
    void givenOrderNumber_whenGetBookInfo_thenThrowBookOrderNotExistException() {
        given(bookOrderRepository.findBookOrderInfo(anyString())).willThrow(BookOrderNotExistException.class);
        Assertions.assertThrows(BookOrderNotExistException.class, () -> bookOrderService.getBookInfo("test"));

        verify(bookOrderRepository, times(1)).findBookOrderInfo(anyString());
    }

    @Test
    @DisplayName("주문 번호로 결제 조회 성공 테스트")
    void givenOrderNumber_whenGetOrderInfoPayment_thenReturnBookOrderPaymentInfoResponse() {
        BookOrderPaymentInfoRespones expected = new BookOrderPaymentInfoRespones("test", "test", "test", "test", "test");
        given(bookOrderRepository.findOrderPayInfo(anyString())).willReturn(Optional.of(expected));
        BookOrderPaymentInfoRespones actual = bookOrderService.getOrderInfoPayment("test");
        Assertions.assertEquals(expected, actual);
        verify(bookOrderRepository, times(1)).findOrderPayInfo(anyString());
    }

    @Test
    @DisplayName("주문 번호로 결제 조회 실패 테스트")
    void givenOrderNumber_whenGetOrderInfoPayment_thenThrowBookOrderNotExistException() {
        given(bookOrderRepository.findOrderPayInfo(anyString())).willThrow(BookOrderNotExistException.class);
        Assertions.assertThrows(BookOrderNotExistException.class, () -> bookOrderService.getOrderInfoPayment("test"));
        verify(bookOrderRepository, times(1)).findOrderPayInfo(anyString());
    }

    @Test
    @DisplayName("주문의 상태 변경 성공 테스트")
    void givenOrderNumber_whenUpdateBookOrderStatus(@Mock BookOrder bookOrder,
                                                    @Mock OrdersStatus ordersStatus) {
        given(bookOrderRepository.findByNumber(anyString())).willReturn(Optional.of(bookOrder));
        given(ordersStatusRepository.findById(anyString())).willReturn(Optional.of(ordersStatus));
        bookOrderService.updateBookOrderStatus("test", BookOrderStatusName.ORDER_COMPLETED);
        verify(bookOrderRepository, times(1)).findByNumber(anyString());
        verify(ordersStatusRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("주문의 상태 변경 실패 테스트(주문 번호가 없는 경우)")
    void givenOrderNumber_whenUpdateBookOrderStatus_thenThrowBookOrderNotExistException() {
        given(bookOrderRepository.findByNumber(anyString())).willThrow(BookOrderNotExistException.class);
        Assertions.assertThrows(BookOrderNotExistException.class, () -> bookOrderService.updateBookOrderStatus("test", BookOrderStatusName.ORDER_COMPLETED));
        verify(bookOrderRepository, times(1)).findByNumber(anyString());
        verify(ordersStatusRepository, never()).findById(any());

    }

    @Test
    @DisplayName("주문의 상태 변경 실패 테스트(주문 상태가 없는 경우)")
    void givenOrderNumber_whenUpdateBookOrderStatus_thenThrowOrdersStatusNotExistException(@Mock BookOrder bookOrder) {
        given(bookOrderRepository.findByNumber(anyString())).willReturn(Optional.of(bookOrder));
        given(ordersStatusRepository.findById(anyString())).willThrow(OrdersStatusNotExistException.class
        );
        Assertions.assertThrows(OrdersStatusNotExistException.class, () -> bookOrderService.updateBookOrderStatus("test", BookOrderStatusName.ORDER_COMPLETED));
        verify(bookOrderRepository, times(1)).findByNumber(anyString());
        verify(ordersStatusRepository, times(1)).findById(any());

    }


    @Test
    @DisplayName("쿠폰이 있는 경우 테스트")
    void givenOrderDetailCreateResponseList_whenCheckCouponUsed_thenReturnTrue() {
        OrderDetailCreateResponse response = new OrderDetailCreateResponse("test", "test", "test", true);
        List<OrderDetailCreateResponse> expected = List.of(response);
        Assertions.assertTrue(bookOrderService.checkCouponUsed(expected));
    }

    @Test
    @DisplayName("쿠폰이 없는 경우 테스트")
    void givenOrderDetailCreateResponseList_whenCheckCouponUsed_thenReturnFalse() {
        OrderDetailCreateResponse response = new OrderDetailCreateResponse("test", "test", "test", false);
        List<OrderDetailCreateResponse> expected = List.of(response);
        Assertions.assertFalse(bookOrderService.checkCouponUsed(expected));
    }
}