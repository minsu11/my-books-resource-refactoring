package store.mybooks.resource.return_rule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.return_rule.dto.response<br>
 * fileName       : ReturnRuleResponse<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 반품 규정을 반환해주는 dto
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
public class ReturnRuleResponse {
    private String returnName;
    private Integer deliveryFee;
    private Integer term;
    private Boolean isAvailable;

}
