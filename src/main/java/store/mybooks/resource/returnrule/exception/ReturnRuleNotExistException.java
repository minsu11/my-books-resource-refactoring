package store.mybooks.resource.returnrule.exception;

/**
 * packageName    : store.mybooks.resource.return_rule.exception<br>
 * fileName       : ReturnRuleNotExistException<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 반품 규정이 존재하지 않는 경우의 {@code exception}
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public class ReturnRuleNotExistException extends RuntimeException {
    public ReturnRuleNotExistException() {
        super("반품 규정이 존재하지 않음");
    }

}
