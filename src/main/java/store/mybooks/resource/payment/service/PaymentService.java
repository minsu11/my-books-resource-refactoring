package store.mybooks.resource.payment.service;

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


    public PayCreateResponse createPayment(PayCreateRequest request, Long userId) {
        BookOrder bookOrder = bookOrderRepository.findByNumber(request.getOrderNumber())
                .orElseThrow(BookOrderNotExistException::new);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));
        Payment payment =
                Payment.builder()
                        .bookOrder(bookOrder)
                        .createdAt(request.getRequestAt())
                        .buyer(user.getName())
                        .cost(request.getTotalCost())
                        .type(request.getType())
                        .build();
        return paymentMapper.mapToPayCreateRequest(payment);
    }
}
