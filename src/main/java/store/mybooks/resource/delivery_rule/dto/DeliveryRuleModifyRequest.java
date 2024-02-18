package store.mybooks.resource.delivery_rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto
 * fileName       : DeliveryRuleModifyRequest
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRuleModifyRequest {
    private Integer deliveryNameRuleId;
    private String deliveryRuleCompanyName;
    private Integer deliveryCost;
    private Integer deliveryRuleCost;
}