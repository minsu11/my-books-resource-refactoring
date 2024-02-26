package store.mybooks.resource.return_rule.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.return_rule.dto.request<br>
 * fileName       : ReturnRuleCreateRequest<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 등록할 정보가 들어 있는 반품 규정 DTO
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class ReturnRuleCreateRequest {
    @NotBlank
    private String returnName;
    @Min(0)
    @Max(1000)
    private Integer deliveryFee;
    @Min(1)
    @Max(365)
    private Integer term;

}
