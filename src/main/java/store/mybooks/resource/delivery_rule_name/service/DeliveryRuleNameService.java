package store.mybooks.resource.delivery_rule_name.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.mapper.DeliveryRuleNameMapper;
import store.mybooks.resource.delivery_rule_name.dto.request.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameAlreadyExistsException;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameNotExistsException;
import store.mybooks.resource.delivery_rule_name.repository.DeliveryRuleNameRepository;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.service<br>
 * fileName       : DeliveryNameRuleService<br>
 * author         : Fiat_lux<br>
 * date           : 2/15/24<br>
 * description    : 배송 규칙 이름 등록, 조회, 삭제를 담당하는 서비스<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/15/24        Fiat_lux       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryRuleNameService {

    private final DeliveryRuleNameRepository deliveryRuleNameRepository;
    private final DeliveryRuleNameMapper deliveryRuleNameMapper;


    /**
     * methodName : registerDeliveryNameRule<br>
     * author : Fiat_lux<br>
     * description : 배송 이름 규칙의 생성 요청이 들어 왔을때 {@code request}의 정보 저장<br>
     * <br>
     *
     * @param deliveryRuleNameRegisterRequest the delivery name rule register request
     * @return the delivery name rule response
     */
    @Transactional
    public DeliveryRuleNameResponse registerDeliveryNameRule(
            DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest) {
        if(deliveryRuleNameRepository.existsById(deliveryRuleNameRegisterRequest.getId())){
            throw new DeliveryRuleNameAlreadyExistsException("해당하는 id값의 데이터가 있습니다");
        }
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName(deliveryRuleNameRegisterRequest.getId());
        DeliveryRuleName saveDeliveryNameRule = this.deliveryRuleNameRepository.save(deliveryNameRule);
        return deliveryRuleNameMapper.mapToResponse(saveDeliveryNameRule);
    }

    /**
     * methodName : getDeliveryNameRule<br>
     * author : Fiat_lux<br>
     * description : <br>
     * {@code id}의 배송 이름 규칙 조회<br>
     * {@code id}의 배송 이름 규칙 조회 할 수 없는 경우 {@code DeliveryRuleNameNotFoundException} 던져줌<br>
     * <br>
     *
     * @param id the id
     * @return the delivery name rule dto로 반환
     * @throws DeliveryRuleNameNotExistsException {@code id}의 데이터 조회 할 수 없는 경우
     */
    public DeliveryRuleNameDto getDeliveryNameRule(String id) {
        return this.deliveryRuleNameRepository.findDeliveryRuleNameById(id)
                .orElseThrow(() -> new DeliveryRuleNameNotExistsException("배송 이름 규칙이 존재하지 않습니다"));
    }

    /**
     * methodName : deleteDeliveryNameRule<br>
     * author : Fiat_lux<br>
     * description : <br>
     * {@code id}의 배송 이름 규칙 삭제<br>
     * {@code id}의 배송 이름 규칙 조회 할 수 없는 경우 {@code DeliveryRuleNameNotFoundException} 던져줌<br>
     * <br>
     *
     * @param id the id
     * @return void
     * @thorws DeliveryRuleNameNotFoundException {@code id}의 데이터 조회 할 수 없는 경우
     */
    @Transactional
    public void deleteDeliveryNameRule(String id) {
        if (this.deliveryRuleNameRepository.existsById(id)) {
            this.deliveryRuleNameRepository.deleteById(id);
        } else {
            throw new DeliveryRuleNameNotExistsException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }
}
