package store.mybooks.resource.delivery_rule.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto<br>
 * fileName       : DeliveryRuleModifyRequest<br>
 * author         : Fiat_lux<br>
 * date           : 2/16/24<br>
 * description    : 수정할 정보가 들어 있는 배송 규칙 DTO<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/16/24        Fiat_lux       최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRuleModifyRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String deliveryNameRuleId;

    @NotBlank
    @Size(min = 1, max = 20)
    private String deliveryRuleCompanyName;

    @NotNull
    @Min(0)
    private Integer deliveryCost;

    @NotNull
    @Min(0)
    private Integer deliveryRuleCost;
}