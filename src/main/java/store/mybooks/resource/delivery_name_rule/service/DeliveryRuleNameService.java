package store.mybooks.resource.delivery_name_rule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryRuleNameMapper;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_name_rule.exception.DeliveryRuleNameNotFoundException;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryRuleNameRepository;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.service
 * fileName       : DeliveryNameRuleService
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
@Transactional(readOnly = true)
public class DeliveryRuleNameService {

    private final DeliveryRuleNameRepository deliveryRuleNameRepository;
    private final DeliveryRuleNameMapper deliveryRuleNameMapper;


    /**
     * Register delivery name rule delivery name rule response.
     *
     * @param deliveryRuleNameRegisterRequest the delivery name rule register request
     * @return the delivery name rule response
     */
    @Transactional
    public DeliveryRuleNameResponse registerDeliveryNameRule(
            DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest) {
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName(deliveryRuleNameRegisterRequest.getId());
        DeliveryRuleName saveDeliveryNameRule = this.deliveryRuleNameRepository.save(deliveryNameRule);
        return deliveryRuleNameMapper.mapToResponse(saveDeliveryNameRule);
    }

    /**
     * Gets delivery name rule.
     *
     * @param id the id
     * @return the delivery name rule
     */
    public DeliveryRuleNameDto getDeliveryNameRule(String id) {
        return this.deliveryRuleNameRepository.findDeliveryRuleNameById(id)
                .orElseThrow(() -> new DeliveryRuleNameNotFoundException("배송 이름 규칙이 존재하지 않습니다"));
    }

    /**
     * Delete delivery name rule.
     *
     * @param id the id
     */
    @Transactional
    public void deleteDeliveryNameRule(String id) {
        if (this.deliveryRuleNameRepository.existsById(id)) {
            this.deliveryRuleNameRepository.deleteById(id);
        } else {
            throw new DeliveryRuleNameNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }
}
