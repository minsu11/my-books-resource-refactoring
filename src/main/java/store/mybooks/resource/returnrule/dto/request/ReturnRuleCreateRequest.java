package store.mybooks.resource.returnrule.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class ReturnRuleCreateRequest {
    @Size(min = 4, max = 50)
    @NotBlank
    private String returnName;
    @Min(0)
    @Max(1000)
    @NotNull
    private Integer deliveryFee;
    @Min(1)
    @Max(365)
    @NotNull
    private Integer term;

}
