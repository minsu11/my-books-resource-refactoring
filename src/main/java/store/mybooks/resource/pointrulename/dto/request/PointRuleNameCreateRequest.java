package store.mybooks.resource.pointrulename.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.point_rule_name.dto.request<br>
 * fileName       : PointRuleNameCreateRequest<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PointRuleNameCreateRequest {
    @NotBlank
    @Size(min = 5, max = 50)
    private String id;
}
