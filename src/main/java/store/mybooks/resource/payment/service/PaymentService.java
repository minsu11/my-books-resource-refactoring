package store.mybooks.resource.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.payment.dto.mapper.PaymentMapper;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.entity.Payment;
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
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookOrderRepository bookOrderRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;


    public PayCreateResponse createPayment(PayCreateRequest request) {
        BookOrder bookOrder = bookOrderRepository.findByNumber(request.getOrderNumber())
                .orElseThrow(BookOrderNotExistException::new);
        User user = userRepository.findById(bookOrder.getUser().getId()).orElseThrow(() -> new UserNotExistException(bookOrder.getUser().getId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime requestedAt = LocalDateTime.parse(request.getRequestedAt(), formatter);
        System.out.println("시간 대: " + requestedAt.toString());
        Payment payment =
                Payment.builder()
                        .bookOrder(bookOrder)
                        .createdAt(requestedAt)
                        .buyer(user.getName())
                        .cost(request.getTotalAmount())
                        .orderNumber(request.getOrderNumber())
                        .type(request.getType())
                        .status(request.getStatus())
                        .build();

        return paymentMapper.mapToPayCreateRequest(paymentRepository.save(payment));
    }
}
