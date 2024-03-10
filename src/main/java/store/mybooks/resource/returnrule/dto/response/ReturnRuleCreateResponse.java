package store.mybooks.resource.returnrule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.return_rule.dto.response<br>
 * fileName       : ReturnRuleCreateResponse<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class ReturnRuleCreateResponse {
    private String returnRuleName;
    private Integer deliveryFee;
    private Integer term;
    private Boolean isAvailable;
}
