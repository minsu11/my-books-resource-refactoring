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
import store.mybooks.resource.payment.dto.request.PayModifyRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.dto.response.PayModifyResponse;
import store.mybooks.resource.payment.entity.Payment;
import store.mybooks.resource.payment.exception.PaymentAlreadyExistException;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayCreateResponse createPayment(PayCreateRequest request) {
        if (paymentRepository.existPaymentByOrderNumber(request.getOrderNumber())) {
            throw new PaymentAlreadyExistException();
        }

        BookOrder bookOrder = bookOrderRepository.findByNumber(request.getOrderNumber())
                .orElseThrow(BookOrderNotExistException::new);
        User user = userRepository.findById(bookOrder.getUser().getId()).orElseThrow(() -> new UserNotExistException(bookOrder.getUser().getId()));
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
     * @param request the request
     * @return the pay modify response
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayModifyResponse modifyStatus(PayModifyRequest request) {
        // check, uncheck
        Payment payment = paymentRepository.findByOrderNumber(request.getOrderNumber()).orElse(null);
        System.out.println("결제 내용 : " + payment.getStatus());
        assert payment != null;
        payment.update(request.getStatus());

        return paymentMapper.mapToPayModifyResponse(payment);
    }


}
