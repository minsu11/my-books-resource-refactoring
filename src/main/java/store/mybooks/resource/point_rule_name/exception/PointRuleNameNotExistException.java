package store.mybooks.resource.point_rule_name.exception;

/**
 * packageName    : store.mybooks.resource.point_rule_name.exception<br>
 * fileName       : PointRuleNameNotExistException<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
public class PointRuleNameNotExistException extends RuntimeException {
    public PointRuleNameNotExistException() {
        super("존재하지 않는 포인트 규정 명입니다.");
    }
}
