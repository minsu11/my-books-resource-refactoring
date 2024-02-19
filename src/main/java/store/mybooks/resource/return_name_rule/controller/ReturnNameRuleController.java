package store.mybooks.resource.return_name_rule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.return_name_rule.dto.response.ReturnNameRuleResponse;
import store.mybooks.resource.return_name_rule.service.ReturnNameRuleService;

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
public class ReturnNameRuleController {
    private final ReturnNameRuleService returnNameRuleService;

    @GetMapping("/{name}")
    public ResponseEntity<ReturnNameRuleResponse> getReturnNameRule(@PathVariable(name = "name") String name) {
        ReturnNameRuleResponse response = returnNameRuleService.getReturnNameRule(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
