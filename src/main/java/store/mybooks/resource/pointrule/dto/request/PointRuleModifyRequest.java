package store.mybooks.resource.pointrule.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.point_rule.dto.request<br>
 * fileName       : PointRuleModifyRequest<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    : 수정할 포인트 규정 dto.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PointRuleModifyRequest {
    @NotBlank
    @Size(min = 5, max = 50)
    private String pointRuleName;

    @PositiveOrZero
    @Max(100)
    private Integer rate;

    @PositiveOrZero
    @Max(10000)
    private Integer cost;
}
