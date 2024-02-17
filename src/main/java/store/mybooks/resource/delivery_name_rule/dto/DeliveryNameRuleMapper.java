package store.mybooks.resource.delivery_name_rule.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;

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

@Mapper
public interface DeliveryNameRuleMapper {
    DeliveryNameRuleMapper INSTANCE = Mappers.getMapper(DeliveryNameRuleMapper.class);

    DeliveryNameRuleResponse mapToResponse(DeliveryNameRule deliveryNameRule);
}
