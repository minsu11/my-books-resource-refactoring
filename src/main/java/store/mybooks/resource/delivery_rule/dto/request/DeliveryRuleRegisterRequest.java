package store.mybooks.resource.delivery_rule.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto<br>
 * fileName       : DeliveryRuleRegisterRequest<br>
 * author         : Fiat_lux<br>
 * date           : 2/16/24<br>
 * description    : 등록할 정보가 들어 있는 배송 규칙 DTO<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/16/24        Fiat_lux       최초 생성<br>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRuleRegisterRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String deliveryNameRuleId;

    @NotBlank
    @Size(min = 1, max = 20)
    private String deliveryRuleCompanyName;

    @Min(0)
    private Integer deliveryCost;

    @Min(0)
    private Integer deliveryRuleCost;
}
