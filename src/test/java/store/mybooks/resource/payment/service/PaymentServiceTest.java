package store.mybooks.resource.payment.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.payment.dto.mapper.PaymentMapper;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.dto.response.PayModifyResponse;
import store.mybooks.resource.payment.dto.response.PaymentResponse;
import store.mybooks.resource.payment.entity.Payment;
import store.mybooks.resource.payment.exception.PaymentAlreadyExistException;
import store.mybooks.resource.payment.exception.PaymentNotExistException;
import store.mybooks.resource.payment.repository.PaymentRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

/**
 * packageName    : store.mybooks.resource.payment.service<br>
 * fileName       : PaymentServiceTest<br>
 * author         : minsu11<br>
 * date           : 3/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/27/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class})
class PaymentServiceTest {
    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private BookOrderRepository bookOrderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentMapper mapper;

    @Test
    @DisplayName("결제 정보 저장")
    void givenPayCreateRequest_whenCreatePayment_thenReturnPayCreateResponse(@Mock BookOrder bookOrder,
                                                                             @Mock User user) {
        PayCreateRequest request = new PayCreateRequest();
        ReflectionTestUtils.setField(request, "orderNumber", "testOrderNumber");
        ReflectionTestUtils.setField(request, "paymentKey", "paymentKey");
        ReflectionTestUtils.setField(request, "status", "status");
        ReflectionTestUtils.setField(request, "requestedAt", "2024-02-20T15:30:00+05:00");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "method", "method");
        PayCreateResponse response = new PayCreateResponse(1L, "paymentKey", 1000);

        given(paymentRepository.existPaymentByOrderNumber(anyString())).willReturn(false);
        given(bookOrderRepository.findByNumber(anyString())).willReturn(Optional.of(bookOrder));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(mapper.mapToPayCreateRequest(any())).willReturn(response);
        PayCreateResponse actual = paymentService.createPayment(request, 1L);

        Assertions.assertEquals(response.getPaymentKey(), actual.getPaymentKey());
        Assertions.assertEquals(response.getTotalAmount(), actual.getTotalAmount());
        verify(paymentRepository, times(1)).existPaymentByOrderNumber(anyString());
        verify(bookOrderRepository, times(1)).findByNumber(anyString());
        verify(userRepository, times(1)).findById(anyLong());
        verify(mapper, times(1)).mapToPayCreateRequest(any());
    }

    @Test
    @DisplayName("결제 정보 저장할 때 이미 결제 정보가 있는 경우")
    void givenPayCreateRequest_whenCreatePayment_thenThrowPaymentAlreadyExistException() {
        PayCreateRequest request = new PayCreateRequest();
        ReflectionTestUtils.setField(request, "orderNumber", "testOrderNumber");
        ReflectionTestUtils.setField(request, "paymentKey", "paymentKey");
        ReflectionTestUtils.setField(request, "status", "status");
        ReflectionTestUtils.setField(request, "requestedAt", "2024-02-20T15:30:00+05:00");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "method", "method");

        given(paymentRepository.existPaymentByOrderNumber(anyString())).willReturn(true);
        Assertions.assertThrows(PaymentAlreadyExistException.class, () -> paymentService.createPayment(request, 1L));
        verify(paymentRepository, times(1)).existPaymentByOrderNumber(anyString());
        verify(bookOrderRepository, never()).findByNumber(anyString());
        verify(userRepository, never()).findById(anyLong());
        verify(mapper, never()).mapToPayCreateRequest(any());
    }

    @Test
    @DisplayName("결제 정보 저장할 때 주문 정보가 없는 경우")
    void givenPayCreateRequest_whenCreatePayment_thenThrowBookOrderNotExistException() {
        PayCreateRequest request = new PayCreateRequest();
        ReflectionTestUtils.setField(request, "orderNumber", "testOrderNumber");
        ReflectionTestUtils.setField(request, "paymentKey", "paymentKey");
        ReflectionTestUtils.setField(request, "status", "status");
        ReflectionTestUtils.setField(request, "requestedAt", "2024-02-20T15:30:00+05:00");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "method", "method");
        given(paymentRepository.existPaymentByOrderNumber(anyString())).willReturn(false);
        given(bookOrderRepository.findByNumber(anyString())).willThrow(BookOrderNotExistException.class);
        Assertions.assertThrows(BookOrderNotExistException.class, () -> paymentService.createPayment(request, 1L));
        verify(bookOrderRepository, times(1)).findByNumber(anyString());
        verify(userRepository, never()).findById(anyLong());
        verify(mapper, never()).mapToPayCreateRequest(any());
    }

    @Test
    @DisplayName("결제 정보 저장할 때 유저가 맞지 않는 경우")
    void givenPayCreateRequest_whenCreatePayment_thenThrowUserNotExistException(@Mock BookOrder bookOrder) {
        PayCreateRequest request = new PayCreateRequest();
        ReflectionTestUtils.setField(request, "orderNumber", "testOrderNumber");
        ReflectionTestUtils.setField(request, "paymentKey", "paymentKey");
        ReflectionTestUtils.setField(request, "status", "status");
        ReflectionTestUtils.setField(request, "requestedAt", "2024-02-20T15:30:00+05:00");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "method", "method");
        given(paymentRepository.existPaymentByOrderNumber(anyString())).willReturn(false);
        given(bookOrderRepository.findByNumber(anyString())).willReturn(Optional.of(bookOrder));
        given(userRepository.findById(anyLong())).willThrow(UserNotExistException.class);
        Assertions.assertThrows(UserNotExistException.class, () -> paymentService.createPayment(request, 1L));
        verify(bookOrderRepository, times(1)).findByNumber(anyString());
        verify(userRepository, times(1)).findById(anyLong());
        verify(mapper, never()).mapToPayCreateRequest(any());
    }

    @Test
    @DisplayName("결제 상태를 변경")
    void givenOrderNumber_whenModifyStatus_thenReturnPayModifyResponse() {
        Payment payment = Payment.builder()
                .orderNumber("test")
                .cost(1000)
                .buyer("test")
                .id(1L)
                .createdAt(LocalDateTime.of(2024, 3, 11, 12, 20))
                .status("DONE")
                .build();

        PayModifyResponse response = new PayModifyResponse("test");
        given(paymentRepository.findByOrderNumber(anyString())).willReturn(Optional.of(payment));
        given(mapper.mapToPayModifyResponse(any())).willReturn(response);
        PayModifyResponse actual = paymentService.modifyStatus("test", "DONE");
        Assertions.assertEquals(response.getStatus(), actual.getStatus());

        verify(paymentRepository, times(1)).findByOrderNumber(anyString());
        verify(mapper, times(1)).mapToPayModifyResponse(any());
    }

    @Test
    @DisplayName("매개변수에 결제 상태가 다른 값이 들어온 경우")
    void givenStatus_whenModifyStatus_thenThrowPaymentNotExistException() {

        Assertions.assertThrows(PaymentNotExistException.class, () -> paymentService.modifyStatus("test", "test"));

        verify(paymentRepository, never()).findByOrderNumber(anyString());
        verify(mapper, never()).mapToPayModifyResponse(any());
    }

    @Test
    @DisplayName("결제 정보를 찾을 수 없는 경우")
    void givenOrderNumber_whenModifyStatus_thenThrowPaymentNotExistException() {

        given(paymentRepository.findByOrderNumber(anyString())).willThrow(PaymentNotExistException.class);
        Assertions.assertThrows(PaymentNotExistException.class, () -> paymentService.modifyStatus("test", "DONE"));

        verify(paymentRepository, times(1)).findByOrderNumber(anyString());
        verify(mapper, never()).mapToPayModifyResponse(any());
    }

    @Test
    @DisplayName("주문 번호로 페이먼츠 키 조회")
    void givenOrderNumber_whenGetPaymentKey_thenReturnPaymentResponse() {
        PaymentResponse expected = new PaymentResponse("test");
        given(paymentRepository.getPaymentKey(anyString())).willReturn(Optional.of(expected));
        PaymentResponse actual = paymentService.getPaymentKey("test");
        Assertions.assertEquals(expected.getPaymentKey(), actual.getPaymentKey());

        verify(paymentRepository, times(1)).getPaymentKey(anyString());
    }

    @Test
    @DisplayName("주문 번호에 맞는 정보가 없는 경우")
    void givenOrderNumber_whenGetPaymentKey_thenThrowPaymentNotExistException() {
        given(paymentRepository.getPaymentKey(anyString())).willThrow(PaymentNotExistException.class);
        Assertions.assertThrows(PaymentNotExistException.class, () -> paymentService.getPaymentKey("test"));

        verify(paymentRepository, times(1)).getPaymentKey(anyString());
    }

}