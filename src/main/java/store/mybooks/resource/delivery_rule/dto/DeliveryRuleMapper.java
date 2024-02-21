package store.mybooks.resource.delivery_rule.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto
 * fileName       : DeliveryRuleMapper
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
@Mapper(componentModel = "spring")
public interface DeliveryRuleMapper {
    /**
     * Map to response delivery rule response.
     *
     * @param deliveryRule the delivery rule
     * @return the delivery rule response
     */
    DeliveryRuleResponse mapToResponse(DeliveryRule deliveryRule);
}
