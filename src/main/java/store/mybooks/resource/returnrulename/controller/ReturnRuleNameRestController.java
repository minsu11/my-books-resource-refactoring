package store.mybooks.resource.returnrulename.controller;


import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.returnrulename.dto.request.ReturnRuleNameCreateRequest;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameCreateResponse;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.exception.ReturnRuleNameRequestValidationFailedException;
import store.mybooks.resource.returnrulename.service.ReturnRuleNameService;

/**
 * packageName    : store.mybooks.resource.return_name_rule.controller<br>
 * fileName       : ReturnNameRuleController<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@RestController
@RequestMapping("/api/return-rule-names")
@RequiredArgsConstructor
public class ReturnRuleNameRestController {
    private final ReturnRuleNameService returnRuleNameService;

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : get 요청 시 name 값에 맞는 데이터를 응답.
     *
     * @param id url 경로로 들어온 요청 데이터
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReturnRuleNameResponse> getReturnRuleName(@PathVariable(name = "id") String id) {
        ReturnRuleNameResponse response = returnRuleNameService.getReturnRuleName(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * methodName : getReturnRuleNameList<br>
     * author : minsu11<br>
     * description : 요청 시 모든 반품 규정 명을 DTO List 응답.
     *
     * @return the return rule name list
     */
    @GetMapping
    public ResponseEntity<List<ReturnRuleNameResponse>> getReturnRuleNameList() {
        List<ReturnRuleNameResponse> returnRuleNameResponseList = returnRuleNameService.getReturnRuleNameList();
        return new ResponseEntity<>(returnRuleNameResponseList, HttpStatus.OK);
    }

    /**
     * methodName : createReturnRuleName<br>
     * author : minsu11<br>
     * description : 요청으로 들어온 반품 규정 명을 저장한 뒤, 반품 규정 명 생성 {@code DTO}로 반환<br>
     * 빈 값 또는 공백 문자만 들어 올 경우 유효성 검사를 통해 ValidationFailedException 던짐.
     *
     * @param request       the request
     * @param bindingResult the binding result
     * @return the response entity
     * @throws ReturnRuleNameRequestValidationFailedException {@code request}가 빈 값 또는 공백 문자만 들어올 경우 throw를 던짐
     */
    @PostMapping
    public ResponseEntity<ReturnRuleNameCreateResponse> createReturnRuleName(
            @Valid @RequestBody ReturnRuleNameCreateRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
        ReturnRuleNameCreateResponse response = returnRuleNameService.createReturnRuleName(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
