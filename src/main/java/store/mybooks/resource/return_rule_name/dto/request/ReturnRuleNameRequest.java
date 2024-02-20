package store.mybooks.resource.return_rule_name.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.return_name_rule.dto.request<br>
 * fileName       : ReturnNameRuleRequest<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    : 반품 규정 명 규칙 요청 데이터<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@Getter
public class ReturnRuleNameRequest {
    @NotNull
    @Size(min = 5, max = 25)
    private String name;
}
