package store.mybooks.resource.return_rule.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.return_rule.dto.request<br>
 * fileName       : ReturnRuleCreateRequest<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Getter
public class ReturnRuleCreateRequest {
    @NotBlank
    private String returnName;
    @Size(min = 0, max = 10000)
    private Integer deliveryFee;
    @Size(min = 0, max = 365)
    private Integer term;
}
