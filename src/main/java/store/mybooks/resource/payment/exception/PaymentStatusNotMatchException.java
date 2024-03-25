package store.mybooks.resource.payment.exception;

/**
 * packageName    : store.mybooks.resource.payment.exception<br>
 * fileName       : PaymentStatusNotMatchException<br>
 * author         : minsu11<br>
 * date           : 3/25/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/25/24        minsu11       최초 생성<br>
 */
public class PaymentStatusNotMatchException extends RuntimeException {
    public PaymentStatusNotMatchException() {
        super("결제 상태가 맞지 않습니다.");
    }
}
