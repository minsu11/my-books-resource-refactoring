package store.mybooks.resource.return_rule_name.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.return_rule_name.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.return_rule_name.service.ReturnRuleNameService;

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
@RequestMapping("/api/return-name-rule")
@RequiredArgsConstructor
public class ReturnNameRuleRestController {
    private final ReturnRuleNameService returnRuleNameService;

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : get 요청 시 name 값에 맞는 데이터를 응답
     *
     * @param name url 경로로 들어온 요청 데이터
     * @return response entity
     */
    @GetMapping("/{name}")
    public ResponseEntity<ReturnRuleNameResponse> getReturnNameRule(@PathVariable(name = "name") String name) {
        ReturnRuleNameResponse response = returnRuleNameService.getReturnNameRule(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
