package store.mybooks.resource.delivery_name_rule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleModifyRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleRegisterRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleResponse;
import store.mybooks.resource.delivery_name_rule.service.DeliveryNameRuleService;

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
@RequestMapping("/api/deliveryNameRules")
public class DeliveryNameRuleController {

    private final DeliveryNameRuleService deliveryNameRuleService;

    /**
     * Instantiates a new Delivery name rule controller.
     *
     * @param deliveryNameRuleService the delivery name rule service
     */
    public DeliveryNameRuleController(DeliveryNameRuleService deliveryNameRuleService) {
        this.deliveryNameRuleService = deliveryNameRuleService;
    }

    /**
     * Gets delivery name rule.
     *
     * @param deliveryNameRuleId the delivery name rule id
     * @return the delivery name rule
     */
    @GetMapping("/{deliveryNameRuleId}")
    public ResponseEntity<DeliveryNameRuleDto> getDeliveryNameRule(
            @PathVariable("deliveryNameRuleId") Integer deliveryNameRuleId) {
        DeliveryNameRuleDto deliveryNameRule = deliveryNameRuleService.getDeliveryNameRule(deliveryNameRuleId);
        return ResponseEntity.ok().body(deliveryNameRule);
    }

    /**
     * Create delivery name rule response entity.
     *
     * @param deliveryNameRuleRegisterRequest the delivery name rule register request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<DeliveryNameRuleResponse> createDeliveryNameRule(
            @RequestBody DeliveryNameRuleRegisterRequest deliveryNameRuleRegisterRequest) {
        DeliveryNameRuleResponse deliveryNameRuleResponse =
                deliveryNameRuleService.registerDeliveryNameRule(deliveryNameRuleRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryNameRuleResponse);
    }

    /**
     * Modify delivery name rule response entity.
     *
     * @param deliveryNameRuleId            the delivery name rule id
     * @param deliveryNameRuleModifyRequest the delivery name rule modify request
     * @return the response entity
     */
    @PutMapping("/{deliveryNameRuleId}")
    public ResponseEntity<DeliveryNameRuleResponse> modifyDeliveryNameRule(
            @PathVariable("deliveryNameRuleId") Integer deliveryNameRuleId,
            @RequestBody DeliveryNameRuleModifyRequest deliveryNameRuleModifyRequest) {
        DeliveryNameRuleResponse deliveryNameRuleResponse =
                deliveryNameRuleService.modifyDeliveryNameRule(deliveryNameRuleId, deliveryNameRuleModifyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(deliveryNameRuleResponse);
    }

    /**
     * Delete delivery name rule response entity.
     *
     * @param deliveryNameRuleId the delivery name rule id
     * @return the response entity
     */
    @DeleteMapping("/{deliveryNameRuleId}")
    public ResponseEntity<Void> deleteDeliveryNameRule(@PathVariable("deliveryNameRuleId") Integer deliveryNameRuleId) {
        deliveryNameRuleService.deleteDeliveryNameRule(deliveryNameRuleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}