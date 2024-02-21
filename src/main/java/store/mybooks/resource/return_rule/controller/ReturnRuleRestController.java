package store.mybooks.resource.return_rule.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
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
     * description : 모든 반품 규정 데이터를 응답
     * <br> *
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<ReturnRuleResponse>> getReturnRuleList() {
        List<ReturnRuleResponse> returnRuleResponses = returnRuleService.getReturnRuleResponseList();
        return new ResponseEntity<>(returnRuleResponses, HttpStatus.OK);
    }


}
