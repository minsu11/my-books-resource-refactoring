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
import store.mybooks.resource.return_rule.dto.response.ReturnRuleDeleteResponse;
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
@RestController
@RequestMapping("/api/return-rules")
@RequiredArgsConstructor
public class ReturnRuleRestController {
    private final ReturnRuleService returnRuleService;


    /**
     * methodName : getReturnRule<br>
     * author : minsu11<br>
     * description : get 요청으로 들어온 name에 대한 조회 데이터 응답
     * <br> *
     *
     * @param returnRuleName 반품 규정을 조회 할 반품 규정 명
     * @return response entity
     */
    @GetMapping("/{returnRuleName}")
    public ResponseEntity<ReturnRuleResponse> getReturnRule(@PathVariable String returnRuleName) {
        ReturnRuleResponse response = returnRuleService.getReturnRuleResponseByReturnRuleName(returnRuleName);
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

    /**
     * methodName : modifyReturnRule<br>
     * author : minsu11<br>
     * description : put 요청으로 들어오는 {@code name}의 정보를 {@code modifyRequest} 정보로 수정.
     * {@code modifyRequest}가 유효성 검사에 어긋난 경우 {@code ReturnRuleValidationFailedException}을 던짐.
     * <br> *
     *
     * @param name          수정 할 정보의 name.
     * @param modifyRequest 수정 할 정보의 {@code request body data}.
     * @param bindingResult 유효성 검사 실패 시 에러의 정보가 담김.
     * @return response entity
     */
    @PutMapping("/{name}")
    public ResponseEntity<ReturnRuleModifyResponse> modifyReturnRule(
            @PathVariable String name,
            @Valid @RequestBody ReturnRuleModifyRequest modifyRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ReturnRuleValidationFailedException(bindingResult);
        }
        ReturnRuleModifyResponse modifyResponse =
                returnRuleService.modifyReturnRule(modifyRequest, name);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * methodName : deleteReturnRule<br>
     * author : minsu11<br>
     * description : {@code id}의 반품 규정에 대한 삭제. 삭제 성공 시 {@code HttpStatus code OK}를 반환
     * <br> *
     *
     * @param id 삭제할 반품 규정
     * @return response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ReturnRuleDeleteResponse> deleteReturnRule(@PathVariable Long id) {
        ReturnRuleDeleteResponse response = returnRuleService.deleteReturnRule(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
