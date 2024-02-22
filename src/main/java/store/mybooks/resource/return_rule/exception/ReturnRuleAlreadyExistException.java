package store.mybooks.resource.return_rule.exception;

/**
 * packageName    : store.mybooks.resource.return_rule.exception<br>
 * fileName       : ReturnRuleAlreadyExistException<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 반품 규정이 이미 존재하는 경우의 {@code exception}
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public class ReturnRuleAlreadyExistException extends RuntimeException {
    public ReturnRuleAlreadyExistException() {
        super("이미 존재하는 반품 규정 명");
    }

    public ReturnRuleAlreadyExistException(String message) {
        super(message);
    }
}
