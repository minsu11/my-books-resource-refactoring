package store.mybooks.resource.payment.exception;

/**
 * packageName    : store.mybooks.resource.payment.exception<br>
 * fileName       : PaymentNotExistException<br>
 * author         : minsu11<br>
 * date           : 3/23/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/23/24        minsu11       최초 생성<br>
 */
public class PaymentNotExistException extends RuntimeException {
    public PaymentNotExistException() {
        super("결제 정보가 없습니다.");
    }
}
