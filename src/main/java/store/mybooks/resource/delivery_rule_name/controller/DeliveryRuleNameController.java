package store.mybooks.resource.delivery_rule_name.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.delivery_rule_name.dto.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_rule_name.dto.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.service.DeliveryRuleNameService;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.controller
 * fileName       : DeliveryNameRuleController
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-name-rules")
public class DeliveryRuleNameController {

    private final DeliveryRuleNameService deliveryNameRuleService;

    /**
     * Gets delivery name rule.
     *
     * @param deliveryNameRuleId the delivery name rule id
     * @return the delivery name rule
     */
    @GetMapping("/{deliveryNameRuleId}")
    public ResponseEntity<DeliveryRuleNameDto> getDeliveryNameRule(
            @PathVariable("deliveryNameRuleId") String deliveryNameRuleId) {
        DeliveryRuleNameDto deliveryNameRule = deliveryNameRuleService.getDeliveryNameRule(deliveryNameRuleId);
        return ResponseEntity.ok().body(deliveryNameRule);
    }

    /**
     * Create delivery name rule response entity.
     *
     * @param deliveryRuleNameRegisterRequest the delivery name rule register request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<DeliveryRuleNameResponse> createDeliveryNameRule(
            @RequestBody DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest) {
        DeliveryRuleNameResponse deliveryRuleNameResponse =
                deliveryNameRuleService.registerDeliveryNameRule(deliveryRuleNameRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryRuleNameResponse);
    }



    /**
     * Delete delivery name rule response entity.
     *
     * @param deliveryNameRuleId the delivery name rule id
     * @return the response entity
     */
    @DeleteMapping("/{deliveryNameRuleId}")
    public ResponseEntity<Void> deleteDeliveryNameRule(@PathVariable("deliveryNameRuleId") String deliveryNameRuleId) {
        deliveryNameRuleService.deleteDeliveryNameRule(deliveryNameRuleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}