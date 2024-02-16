package store.mybooks.resource.delivery_rule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto
 * fileName       : DeliveryRuleRegisterRequest
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
@Getter
@NoArgsConstructor
public class DeliveryRuleRegisterRequest {
    private Integer deliveryNameRuleId;
    private String deliveryCompanyName;
    private Integer deliveryCost;
    private Integer deliveryRuleCost;
}
