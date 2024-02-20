package store.mybooks.resource.delivery_name_rule.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleMapper;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleModifyRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleRegisterRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleResponse;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_name_rule.exception.DeliveryNameRuleNotFoundException;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryNameRuleRepository;

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
public class DeliveryNameRuleService {

    private final DeliveryNameRuleRepository deliveryNameRuleRepository;
    private final DeliveryNameRuleMapper deliveryNameRuleMapper;


    /**
     * Register delivery name rule delivery name rule response.
     *
     * @param deliveryNameRuleRegisterRequest the delivery name rule register request
     * @return the delivery name rule response
     */
    @Transactional
    public DeliveryNameRuleResponse registerDeliveryNameRule(
            DeliveryNameRuleRegisterRequest deliveryNameRuleRegisterRequest) {
        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(deliveryNameRuleRegisterRequest);
        DeliveryNameRule saveDeliveryNameRule = this.deliveryNameRuleRepository.save(deliveryNameRule);
        return deliveryNameRuleMapper.mapToResponse(saveDeliveryNameRule);
    }

    /**
     * Gets delivery name rule.
     *
     * @param id the id
     * @return the delivery name rule
     */
    public DeliveryNameRuleDto getDeliveryNameRule(Integer id) {
        return this.deliveryNameRuleRepository.findDeliveryNameRuleById(id)
                .orElseThrow(() -> new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다"));
    }

    /**
     * Modify delivery name rule delivery name rule response.
     *
     * @param deliveryNameRuleId            the delivery name rule id
     * @param deliveryNameRuleModifyRequest the delivery name rule modify request
     * @return the delivery name rule response
     */
    @Transactional
    public DeliveryNameRuleResponse modifyDeliveryNameRule(Integer deliveryNameRuleId,
                                                           DeliveryNameRuleModifyRequest deliveryNameRuleModifyRequest) {
        Optional<DeliveryNameRule> optionalDeliveryNameRule =
                this.deliveryNameRuleRepository.findById(deliveryNameRuleId);

        if (optionalDeliveryNameRule.isPresent()) {
            DeliveryNameRule deliveryNameRule = optionalDeliveryNameRule.get();
            deliveryNameRule.setName(deliveryNameRuleModifyRequest.getDeliveryName());
            return deliveryNameRuleMapper.mapToResponse(deliveryNameRule);
        } else {
            throw new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }

    /**
     * Delete delivery name rule.
     *
     * @param id the id
     */
    @Transactional
    public void deleteDeliveryNameRule(Integer id) {
        if (this.deliveryNameRuleRepository.existsById(id)) {
            this.deliveryNameRuleRepository.deleteById(id);
        } else {
            throw new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }
}
