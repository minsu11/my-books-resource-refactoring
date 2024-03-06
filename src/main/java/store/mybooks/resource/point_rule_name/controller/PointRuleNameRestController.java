package store.mybooks.resource.point_rule_name.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import store.mybooks.resource.point_rule_name.dto.response.PointRuleNameResponse;
import store.mybooks.resource.point_rule_name.service.PointRuleNameService;

/**
 * packageName    : store.mybooks.resource.point_rule_name.controller<br>
 * fileName       : PointRuleNameRestController<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/point-rule-names")
public class PointRuleNameRestController {
    private final PointRuleNameService pointRuleNameService;

    /**
     * methodName : getPointRuleName<br>
     * author : minsu11<br>
     * description : id에 대한 포인트 이름 명 조회.
     * <br> *
     *
     * @param id
     * @return response entity
     */
    @GetMapping("{id")
    public ResponseEntity<PointRuleNameResponse> getPointRuleName(String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointRuleNameService.getPointRuleName(id));
    }

    @GetMapping
    public ResponseEntity<List<PointRuleNameResponse>> getPointRuleNameList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointRuleNameService.getPointRuleNameList());
    }


}
