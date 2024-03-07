package store.mybooks.resource.point_rule_name.exception;

/**
 * packageName    : store.mybooks.resource.point_rule_name.exception<br>
 * fileName       : PointRuleNameAlreadyExistException<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
public class PointRuleNameAlreadyExistException extends RuntimeException {
    public PointRuleNameAlreadyExistException() {
        super("이미 존재하는 이름입니다.");
    }
}
