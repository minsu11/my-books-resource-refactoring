package store.mybooks.resource.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.payment.dto.mapper.PaymentMapper;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.dto.response.PayModifyResponse;
import store.mybooks.resource.payment.dto.response.PaymentResponse;
import store.mybooks.resource.payment.entity.Payment;
import store.mybooks.resource.payment.enumulation.PaymentStatusEnum;
import store.mybooks.resource.payment.exception.PaymentAlreadyExistException;
import store.mybooks.resource.payment.exception.PaymentNotExistException;
import store.mybooks.resource.payment.repository.PaymentRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

/**
 * packageName    : store.mybooks.resource.payment.service<br>
 * fileName       : PaymentService<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookOrderRepository bookOrderRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    /**
     * methodName : createPayment<br>
     * author : minsu11<br>
     * description : 결제 정보 저장. {@code Roll Back}시 성공한 결제 정보는 저장.
     * <br>
     *
     * @param request the request
     * @return the pay create response
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayCreateResponse createPayment(PayCreateRequest request, Long userId) {
        if (paymentRepository.existPaymentByOrderNumber(request.getOrderNumber())) {
            throw new PaymentAlreadyExistException();
        }
        BookOrder bookOrder = bookOrderRepository.findByNumber(request.getOrderNumber())
                .orElseThrow(BookOrderNotExistException::new);
        User user = userRepository.findById(
                        userId)
                .orElseThrow(() -> new UserNotExistException(bookOrder.getUser().getId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime requestedAt = LocalDateTime.parse(request.getRequestedAt(), formatter);
        Payment payment =
                Payment.builder()
                        .bookOrder(bookOrder)
                        .createdAt(requestedAt)
                        .buyer(user.getName())
                        .cost(request.getTotalAmount())
                        .orderNumber(request.getPaymentKey())
                        .type(request.getMethod())
                        .status(request.getStatus())
                        .build();

        return paymentMapper.mapToPayCreateRequest(paymentRepository.save(payment));
    }

    /**
     * methodName : modifyStatus<br>
     * author : minsu11<br>
     * description : 결제 결과에 대한 상태 값 변경.
     * <br>
     *
     * @param orderNumber the request
     * @param status      상태
     * @return the pay modify response
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayModifyResponse modifyStatus(String orderNumber, String status) {

        if (!PaymentStatusEnum.DONE.getEngPaymentStatus().equals(status)
                && !PaymentStatusEnum.CANCELED.getEngPaymentStatus().equals(status)) {
            throw new PaymentNotExistException();
        }
        Payment payment = paymentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(PaymentNotExistException::new);
        payment.update(status);

        return paymentMapper.mapToPayModifyResponse(payment);
    }

    /**
     * methodName : getPaymentKey<br>
     * author : minsu11<br>
     * description : 주문 번호로 toss payment key 조회.
     * <br>
     *
     * @param orderNumber 주문 번호
     * @return the payment key
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentKey(String orderNumber) {
        return paymentRepository.getPaymentKey(orderNumber)
                .orElseThrow(PaymentNotExistException::new);

    }
}
