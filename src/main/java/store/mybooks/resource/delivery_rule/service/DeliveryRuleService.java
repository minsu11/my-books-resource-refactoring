package store.mybooks.resource.delivery_rule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameNotFoundException;
import store.mybooks.resource.delivery_rule_name.repository.DeliveryRuleNameRepository;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleMapper;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotFoundException;
import store.mybooks.resource.delivery_rule.repository.DeliveryRuleRepository;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_rule.service
 * fileName       : DeliveryRuleService
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
@Service
@RequiredArgsConstructor
public class DeliveryRuleService {

    private final DeliveryRuleRepository deliveryRuleRepository;
    private final DeliveryRuleNameRepository deliveryRuleNameRepository;
    private final DeliveryRuleMapper deliveryRuleMapper;

    /**
     * Register delivery rule delivery rule response.
     *
     * @param deliveryRuleRegisterRequest the delivery rule register request
     * @return the delivery rule response
     */
    @Transactional
    public DeliveryRuleResponse registerDeliveryRule(DeliveryRuleRegisterRequest deliveryRuleRegisterRequest) {

        DeliveryRuleName deliveryRuleName =
                deliveryRuleNameRepository.findById(deliveryRuleRegisterRequest.getDeliveryNameRuleId())
                        .orElseThrow(() -> new DeliveryRuleNameNotFoundException("배송 이름 규칙이 존재하지 않습니다."));

        DeliveryRule deliveryRule = new DeliveryRule(deliveryRuleName,
                deliveryRuleRegisterRequest.getDeliveryCompanyName(), deliveryRuleRegisterRequest.getDeliveryCost(),
                deliveryRuleRegisterRequest.getDeliveryRuleCost());

        DeliveryRule saveDeliveryRule = deliveryRuleRepository.save(deliveryRule);
        return deliveryRuleMapper.mapToResponse(saveDeliveryRule);

    }

    /**
     * Gets delivery rule.
     *
     * @param id the id
     * @return the delivery rule
     */
    @Transactional(readOnly = true)
    public DeliveryRuleDto getDeliveryRule(Integer id) {

        return deliveryRuleRepository.findDeliveryRuleById(id)
                .orElseThrow(() -> new DeliveryRuleNotFoundException("배송 규칙이 존재하지 않습니다"));
    }

    /**
     * Modify delivery rule delivery rule response.
     *
     * @param id                        the id
     * @param deliveryRuleModifyRequest the delivery rule modify request
     * @return the delivery rule response
     */
    @Transactional
    public DeliveryRuleResponse modifyDeliveryRule(Integer id, DeliveryRuleModifyRequest deliveryRuleModifyRequest) {
        DeliveryRule deliveryRule = deliveryRuleRepository.findById(id)
                .orElseThrow(() -> new DeliveryRuleNotFoundException("배송 규칙이 존재하지 않습니다."));

        DeliveryRuleName deliveryRuleName =
                deliveryRuleNameRepository.findById(deliveryRuleModifyRequest.getDeliveryNameRuleId())
                        .orElseThrow(() -> new DeliveryRuleNameNotFoundException("배송 규칙이 존재하지 않습니다"));

        deliveryRule.setDeliveryRuleName(deliveryRuleName);
        deliveryRule.setCompanyName(deliveryRuleModifyRequest.getDeliveryRuleCompanyName());
        deliveryRule.setCost(deliveryRuleModifyRequest.getDeliveryCost());
        deliveryRule.setRuleCost(deliveryRuleModifyRequest.getDeliveryRuleCost());
        return deliveryRuleMapper.mapToResponse(deliveryRule);
    }


    /**
     * Delete delivery rule.
     *
     * @param id the id
     */
    @Transactional
    public void deleteDeliveryRule(Integer id) {
        if (deliveryRuleRepository.existsById(id)) {
            deliveryRuleRepository.deleteById(id);
        } else {
            throw new DeliveryRuleNotFoundException("배송 규칙이 존재하지 않습니다");
        }
    }

}
