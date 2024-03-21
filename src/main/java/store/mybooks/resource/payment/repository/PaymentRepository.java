package store.mybooks.resource.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.payment.entity.Payment;

/**
 * packageName    : store.mybooks.resource.payment.repository<br>
 * fileName       : PaymentRepository<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
}
