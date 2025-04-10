package store.mybooks.resource.pointrule.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.point_rule.dto.request<br>
 * fileName       : PointRuleCreateRequest<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    : 등록할 포인트 규정 dto.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PointRuleCreateRequest {
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
