package store.mybooks.resource.returnrule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.return_rule.dto.response<br>
 * fileName       : ReturnRuleModifyResponse<br>
 * author         : minsu11<br>
 * date           : 2/22/24<br>
 * description    : 수정 된 반품 규정 DTO
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/22/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class ReturnRuleModifyResponse {
    private String returnRuleNameId;
    private Integer deliveryFee;
    private Integer term;
    private Boolean isAvailable;

}
