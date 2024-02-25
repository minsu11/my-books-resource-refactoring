package store.mybooks.resource.delivery_rule.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleValidationFailedException;
import store.mybooks.resource.delivery_rule.service.DeliveryRuleService;

/**
 * packageName    : store.mybooks.resource.delivery_rule.controller<br>
 * fileName       : DeliveryRuleController<br>
 * author         : Fiat_lux<br>
 * date           : 2/16/24<br>
 * description    :<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/16/24        Fiat_lux       최초 생성<br>
 */
@RestController
@RequestMapping("/api/delivery-rules")
public class DeliveryRuleController {
    private final DeliveryRuleService deliveryRuleService;

    /**
     * Instantiates a new Delivery rule controller.
     *
     * @param deliveryRuleService the delivery rule service
     */
    public DeliveryRuleController(DeliveryRuleService deliveryRuleService) {
        this.deliveryRuleService = deliveryRuleService;
    }

    /**
     * methodName : getDeliveryRule<br>
     * author : Fiat_lux<br>
     * description : get 요청 으로 들어온 deliveryRuleId에 대한 조회 데이터 응답<br>
     * <br>
     *
     * @param deliveryRuleId the delivery rule id
     * @return the delivery rule
     */
    @GetMapping("/{deliveryRuleId}")
    public ResponseEntity<DeliveryRuleDto> getDeliveryRule(
            @PathVariable("deliveryRuleId") Integer deliveryRuleId) {
        DeliveryRuleDto deliveryRule = deliveryRuleService.getDeliveryRule(deliveryRuleId);

        return ResponseEntity.ok().body(deliveryRule);
    }

    /**
     * methodName : createDeliveryRule<br>
     * author : Fiat_lux<br>
     * description : post 요청 시 {@code request}의 정보를 DB에 저장 시킨후 DB에 저장된 데이터를 DTO로 반환 <br>
     * {@code request}가 유효성 검사 실패 시 {@code DeliveryRuleValidationFailedException}을 던짐<br>
     * <br>
     *
     * @param deliveryRuleRegisterRequest post 요청으로 들어오는 {@code RequestBody}<br>
     * @param bindingResult               유효성 검사 실패 시 발생한 오류에 대한 데이터 가지고 있음 <br>
     * @return the response entity<br>
     * @throws DeliveryRuleValidationFailedException {@code request} 가 유효성 검사 실패 시 발생<br>
     */
    @PostMapping
    public ResponseEntity<DeliveryRuleResponse> createDeliveryRule(
            @Valid @RequestBody DeliveryRuleRegisterRequest deliveryRuleRegisterRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DeliveryRuleValidationFailedException(bindingResult);
        }
        DeliveryRuleResponse deliveryRuleResponse =
                deliveryRuleService.registerDeliveryRule(deliveryRuleRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryRuleResponse);
    }

    /**
     * methodName : modifyDeliveryRule<br>
     * author : Fiat_lux<br>
     * description : put 요청으로 들어오는 {@code id}의 정보를 {@code modifyRequest} 정보로 수정<br>
     * {@code modifyRequest}가 유효성 검사에 어긋난 경우 {@code DeliveryRuleValidationFailedException} 던짐<br>
     * <br>
     *
     * @param deliveryRuleId            the delivery rule id
     * @param deliveryRuleModifyRequest the delivery rule modify request
     * @param bindingResult             유효성 검사 실패 시 에러의 정보가 담김
     * @return the response entity
     * @throws DeliveryRuleValidationFailedException {@code request} 가 유효성 검사 실패 시 발생<br>
     */
    @PutMapping("/{deliveryRuleId}")
    public ResponseEntity<DeliveryRuleResponse> modifyDeliveryRule(
            @PathVariable("deliveryRuleId") Integer deliveryRuleId,
            @Valid @RequestBody DeliveryRuleModifyRequest deliveryRuleModifyRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DeliveryRuleValidationFailedException(bindingResult);
        }
        DeliveryRuleResponse deliveryRuleResponse =
                deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest);

        return ResponseEntity.status(HttpStatus.OK).body(deliveryRuleResponse);
    }

    /**
     * methodName : modifyDeliveryRule<br>
     * author : Fiat_lux<br>
     * description : delete 요청으로 들어온 deliveryRuleId에 대한 데이터 삭제
     *
     * @param deliveryRuleId the delivery rule id
     * @return the response entity
     */
    @DeleteMapping("/{deliveryRuleId}")
    public ResponseEntity<Void> deleteDeliveryRule(@PathVariable("deliveryRuleId") Integer deliveryRuleId) {
        deliveryRuleService.deleteDeliveryRule(deliveryRuleId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
