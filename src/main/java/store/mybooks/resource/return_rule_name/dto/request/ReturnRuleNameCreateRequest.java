package store.mybooks.resource.return_rule_name.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.return_rule_name.dto.request<br>
 * fileName       : ReturnRulsNameCreateRequest<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class ReturnRuleNameCreateRequest {

    @NotBlank
    @Size(min = 2, max = 10)
    private String id;
}
