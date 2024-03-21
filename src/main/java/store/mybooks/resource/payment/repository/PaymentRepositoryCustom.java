package store.mybooks.resource.payment.repository;

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.payment.entity.Payment;

/**
 * packageName    : store.mybooks.resource.payment.repository<br>
 * fileName       : PaymentRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface PaymentRepositoryCustom {
    Optional<Payment> findByOrderNumber(String orderNumber);

    boolean existPaymentByOrderNumber(String orderNumber);
}
