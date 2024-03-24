package store.mybooks.resource.payment.exception;

/**
 * packageName    : store.mybooks.resource.payment.exception<br>
 * fileName       : PaymentAlreadyExistException<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
public class PaymentAlreadyExistException extends RuntimeException {
    public PaymentAlreadyExistException() {
        super("이미 존재하는 결제 내역입니다.");
    }
}
