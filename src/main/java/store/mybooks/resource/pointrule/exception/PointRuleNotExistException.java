package store.mybooks.resource.pointrule.exception;

/**
 * packageName    : store.mybooks.resource.point_rule.exception<br>
 * fileName       : PointRuleNotExistException<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
public class PointRuleNotExistException extends RuntimeException {
    public PointRuleNotExistException() {
        super("포인트 규정이 존재하지 않음");
    }
}
