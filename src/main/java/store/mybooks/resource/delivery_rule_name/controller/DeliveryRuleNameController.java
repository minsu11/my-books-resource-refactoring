package store.mybooks.resource.delivery_rule_name.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.delivery_rule_name.dto.request.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.service.DeliveryRuleNameService;
import store.mybooks.resource.error.RequestValidationFailedException;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.controller<br>
 * fileName       : DeliveryNameRuleController<br>
 * author         : Fiat_lux<br>
 * date           : 2/16/24<br>
 * description    :<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/16/24        Fiat_lux       최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-name-rules")
public class DeliveryRuleNameController {

    private final DeliveryRuleNameService deliveryNameRuleService;

    /**
     * methodName : getDeliveryNameRuleList<br>
     * author : Fiat_lux<br>
     * description : get 요청으로 조회 데이터 list 응답<br>
     *
     * @return deliveryRuleNameResponse list<br>
     */
    @GetMapping
    public ResponseEntity<List<DeliveryRuleNameResponse>> getDeliveryNameRuleList() {
        List<DeliveryRuleNameResponse> deliveryNameRuleList = deliveryNameRuleService.getDeliveryNameRuleList();
        return ResponseEntity.ok().body(deliveryNameRuleList);
    }

    /**
     * methodName : getDeliveryNameRule<br>
     * author : Fiat_lux<br>
     * description : get 요청으로 파라미터 값 id에 대한 조회 데이터 응답<br>
     *
     * @param deliveryNameRuleId the delivery name rule id<br>
     * @return the delivery name rule<br>
     */
    @GetMapping("/{deliveryNameRuleId}")
    public ResponseEntity<DeliveryRuleNameDto> getDeliveryNameRule(
            @PathVariable("deliveryNameRuleId") String deliveryNameRuleId) {
        DeliveryRuleNameDto deliveryNameRule = deliveryNameRuleService.getDeliveryNameRule(deliveryNameRuleId);
        return ResponseEntity.ok().body(deliveryNameRule);
    }

    /**
     * methodName : createDeliveryNameRule<br>
     * author : Fiat_lux<br>
     * description : post 요청시 {@code request}의 정보를 DB에 저장 시킨 후 DB에 저장된 데이터를 DTO로 반환<br>
     * {@code request}가 유효성 검사 실패 시 {@code DeliveryRuleNameValidationFailedException}을 던짐<br>
     * <br>
     *
     * @param deliveryRuleNameRegisterRequest the delivery name rule register request<br>
     * @param bindingResult                   유효성 검사 실패 시 발생한 오류에 대한 데이터를 가지고 있다<br>
     * @return the response entity<br>
     * @throws RequestValidationFailedException {@code request} 가 유효성 검사 실패시 던짐 <br>
     */
    @PostMapping
    public ResponseEntity<DeliveryRuleNameResponse> createDeliveryNameRule(
            @Valid @RequestBody DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
        DeliveryRuleNameResponse deliveryRuleNameResponse =
                deliveryNameRuleService.registerDeliveryNameRule(deliveryRuleNameRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryRuleNameResponse);
    }


    /**
     * methodName : deleteDeliveryNameRule<br>
     * author : Fiat_lux<br>
     * description : delete 요청으로 파라미터 값 id에 대한 데이터 삭제<br>
     *
     * @param deliveryNameRuleId the delivery name rule id
     * @return void
     */
    @DeleteMapping("/{deliveryNameRuleId}")
    public ResponseEntity<Void> deleteDeliveryNameRule(@PathVariable("deliveryNameRuleId") String deliveryNameRuleId) {
        deliveryNameRuleService.deleteDeliveryNameRule(deliveryNameRuleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}