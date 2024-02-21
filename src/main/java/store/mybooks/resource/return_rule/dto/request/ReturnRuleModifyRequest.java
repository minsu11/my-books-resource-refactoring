package store.mybooks.resource.return_rule.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.return_rule.dto.request<br>
 * fileName       : ReturnRuleModifyRequest<br>
 * author         : minsu11<br>
 * date           : 2/22/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/22/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class ReturnRuleModifyRequest {
    @NotBlank
    private String returnRuleNameId;
    @Size(min = 0, max = 10000)
    private Integer deliveryFee;
    @Size(min = 0, max = 365)
    private Integer term;
    private Boolean isAvailable;

}
