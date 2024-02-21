package store.mybooks.resource.delivery_name_rule.dto;

import org.mapstruct.Mapper;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.dto
 * fileName       : DeliveryNameRuleMapper
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */

@Mapper(componentModel = "spring")
public interface DeliveryRuleNameMapper {

    /**
     * Map to response delivery rule name response.
     *
     * @param deliveryRuleName the delivery rule name
     * @return the delivery rule name response
     */
    DeliveryRuleNameResponse mapToResponse(DeliveryRuleName deliveryRuleName);
}
