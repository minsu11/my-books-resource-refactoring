package store.mybooks.resource.return_name_rule.exception;

/**
 * packageName    : store.mybooks.resource.return_name_rule.exception<br>
 * fileName       : ReturnNameRuleNotFoundException<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    : 반품 규정명 규칙에 대한 데이터가 없을 경우의 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        minsu11       최초 생성
 */
public class ReturnNameRuleNotFoundException extends RuntimeException {
    public ReturnNameRuleNotFoundException() {
    }

    public ReturnNameRuleNotFoundException(String message) {
        super(message);
    }
}
