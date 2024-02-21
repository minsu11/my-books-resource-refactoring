package store.mybooks.resource.return_rule_name.exception;

/**
 * packageName    : store.mybooks.resource.return_rule_name.exception<br>
 * fileName       : ReturnRuleNameAlreadyExixtException<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public class ReturnRuleNameAlreadyExistException extends RuntimeException {
    public ReturnRuleNameAlreadyExistException() {
    }

    public ReturnRuleNameAlreadyExistException(String message) {
        super(message);
    }
}
