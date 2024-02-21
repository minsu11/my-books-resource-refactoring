package store.mybooks.resource.return_rule.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleCreateRequest;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleModifyRequest;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleCreateResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleModifyResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.exception.ReturnRuleValidationFailedException;
import store.mybooks.resource.return_rule.service.ReturnRuleService;

/**
 * packageName    : store.mybooks.resource.return_rule.controller<br>
 * fileName       : ReturnRuleRestController<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@RequestMapping("/api/return-rule")
@RequiredArgsConstructor
public class ReturnRuleRestController {
    private final ReturnRuleService returnRuleService;


    /**
     * methodName : getReturnRule<br>
     * author : minsu11<br>
     * description : get 요청으로 들어온 name에 대한 조회 데이터 응답
     * <br> *
     *
     * @param name
     * @return response entity
     */
    @GetMapping("/{name}")
    public ResponseEntity<ReturnRuleResponse> getReturnRule(@PathVariable String name) {
        ReturnRuleResponse response = returnRuleService.getReturnRuleResponseByReturnRuleName(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * methodName : getReturnRuleList<br>
     * author : minsu11<br>
     * description : api get 요청 시 모든 반품 규정을 리스트로 반환<br>
     * DB에 저장된 데이터가 없을 시 빈 리스트를 반환 하므로 주의
     * <br> *
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<ReturnRuleResponse>> getReturnRuleList() {
        List<ReturnRuleResponse> returnRuleResponses = returnRuleService.getReturnRuleResponseList();
        return new ResponseEntity<>(returnRuleResponses, HttpStatus.OK);
    }

    /**
     * methodName : createReturnRule<br>
     * author : minsu11<br>
     * description : post 요청 시 {@code request}의 정보를 DB에 저장 시킨 후
     * DB에 저장된 데이터를 DTo로 반환해줌.<br>
     * {@code request}가 유효성 검사 실패 시 {@code ReturnRuleNameRequestValidationFailedException}을 던져줌
     * <br> *
     *
     * @param request       post 요청으로 들어오는 {@code RequestBody}<br>
     * @param bindingResult 유효성 검사 실패 시 발생한 오류에 대한 데이터를 가지고 있음<br>
     * @return response entity<br>
     * @throws ReturnRuleValidationFailedException {@code request}가 유효성 검사 실패 시 던짐<br>
     */
    @PostMapping
    public ResponseEntity<ReturnRuleCreateResponse> createReturnRule(
            @Valid @RequestBody ReturnRuleCreateRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ReturnRuleValidationFailedException(bindingResult);
        }
        ReturnRuleCreateResponse response = returnRuleService.createReturnRule(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public ResponseEntity<ReturnRuleModifyResponse> modifyReturnRule(
            @PathVariable String id,
            @Valid @RequestBody ReturnRuleModifyRequest modifyRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ReturnRuleValidationFailedException(bindingResult);
        }
        ReturnRuleModifyResponse modifyResponse =
                returnRuleService.modifyReturnRule(modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }
}
