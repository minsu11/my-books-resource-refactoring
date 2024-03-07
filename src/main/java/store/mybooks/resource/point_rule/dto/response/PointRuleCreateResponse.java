package store.mybooks.resource.point_rule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.point_rule.dto.response<br>
 * fileName       : PointRuleCreateResponse<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    : 등록한 포인트 규정 dto.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class PointRuleCreateResponse {
    private String pointRuleName;
    private Integer rate;
    private Integer cost;
}
