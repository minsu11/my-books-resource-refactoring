package store.mybooks.resource.delivery_rule.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_name_rule.exception.DeliveryNameRuleNotFoundException;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryNameRuleRepository;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleMapper;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotFoundException;
import store.mybooks.resource.delivery_rule.repository.DeliveryRuleRepository;

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
    private final DeliveryNameRuleRepository deliveryNameRuleRepository;
    private final DeliveryRuleMapper deliveryRuleMapper;

    @Transactional
    public void registerDeliveryRule(DeliveryRuleRegisterRequest deliveryRuleRegisterRequest) {
        Optional<DeliveryNameRule> optionalDeliveryNameRule =
                deliveryNameRuleRepository.findById(deliveryRuleRegisterRequest.getDeliveryNameRuleId());

        if (optionalDeliveryNameRule.isPresent()) {
            DeliveryNameRule deliveryNameRule = optionalDeliveryNameRule.get();
            DeliveryRule deliveryRule = new DeliveryRule(deliveryRuleRegisterRequest, deliveryNameRule);

            deliveryRuleRepository.save(deliveryRule);
        } else {
            throw new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public DeliveryRuleResponse getDeliveryRule(Integer id) {
        Optional<DeliveryRule> optionalDeliveryRule = deliveryRuleRepository.findDeliveryRuleById(id);

        if (optionalDeliveryRule.isPresent()) {
            DeliveryRule deliveryRule = optionalDeliveryRule.get();

            return deliveryRuleMapper.mapToResponse(deliveryRule);
        } else {
            throw new DeliveryRuleNotFoundException("배송 규칙이 존재하지 않습니다");
        }
    }

    @Transactional
    public void modifyDeliveryRule(Integer id, DeliveryRuleModifyRequest deliveryRuleModifyRequest) {
        Optional<DeliveryRule> optionalDeliveryRule = deliveryRuleRepository.findById(id);
        Optional<DeliveryNameRule> optionalDeliveryNameRule =
                deliveryNameRuleRepository.findById(deliveryRuleModifyRequest.getDeliveryNameRuleId());

        if (optionalDeliveryRule.isPresent() && optionalDeliveryNameRule.isPresent()) {
            DeliveryRule deliveryRule = optionalDeliveryRule.get();
            DeliveryNameRule deliveryNameRule = optionalDeliveryNameRule.get();
            deliveryRule.setDeliveryRule(deliveryRuleModifyRequest, deliveryNameRule);
        } else {
            throw new DeliveryRuleNotFoundException("배송 규칙이 존재하지 않습니다");
        }
    }

    @Transactional
    public void deleteDeliveryRule(Integer id) {
        if (deliveryRuleRepository.existsById(id)) {
            deliveryRuleRepository.deleteById(id);
        } else {
            throw new DeliveryRuleNotFoundException("배송 규칙이 존재하지 않습니다");
        }
    }

}
