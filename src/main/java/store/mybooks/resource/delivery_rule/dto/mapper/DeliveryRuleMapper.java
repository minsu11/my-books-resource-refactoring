package store.mybooks.resource.delivery_rule.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
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
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeliveryRuleMapper {
    /**
     * Map to response delivery rule response.
     *
     * @param deliveryRule the delivery rule
     * @return the delivery rule response
     */
    @Mapping(target = "deliveryRuleNameId", source = "deliveryRuleName.id")
    DeliveryRuleResponse mapToResponse(DeliveryRule deliveryRule);
}
