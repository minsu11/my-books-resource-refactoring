package store.mybooks.resource.return_rule.exception;

/**
 * packageName    : store.mybooks.resource.return_rule.exception<br>
 * fileName       : ReturnRuleNotExistException<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public class ReturnRuleNotExistException extends RuntimeException {
    public ReturnRuleNotExistException() {
    }

    public ReturnRuleNotExistException(String message) {
        super(message);
    }
}
