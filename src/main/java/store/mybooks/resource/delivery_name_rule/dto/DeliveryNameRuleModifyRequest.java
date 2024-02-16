package store.mybooks.resource.delivery_name_rule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.dto
 * fileName       : DeliveryNameRuleModifyRequest
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNameRuleModifyRequest {
    String deliveryName;
}