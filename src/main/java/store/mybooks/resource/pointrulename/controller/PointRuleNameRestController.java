package store.mybooks.resource.pointrulename.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.error.Utils;
import store.mybooks.resource.pointrulename.dto.request.PointRuleNameCreateRequest;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameCreateResponse;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;

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
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point-rule-names")
public class PointRuleNameRestController {
    private final PointRuleNameService pointRuleNameService;

    /**
     * methodName : getPointRuleName<br>
     * author : minsu11<br>
     * description : id에 대한 포인트 규정 명 조회.
     * <br> *
     *
     * @param id
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointRuleNameResponse> getPointRuleName(@PathVariable(name = "id") String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointRuleNameService.getPointRuleName(id));
    }

    /**
     * methodName : getPointRuleNameList<br>
     * author : minsu11<br>
     * description : 전체 포인트 규정 명 조회.
     * <br> *
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<PointRuleNameResponse>> getPointRuleNameList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointRuleNameService.getPointRuleNameList());
    }

    /**
     * methodName : createPointRuleName<br>
     * author : minsu11<br>
     * description : 포인트 규정 명 생성.
     * <br> *
     *
     * @param request       생성할 포인트 규정 명.
     * @param bindingResult 유효성 검사
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<PointRuleNameCreateResponse> createPointRuleName(
            @Valid @RequestBody PointRuleNameCreateRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pointRuleNameService.createPointRuleName(request));
    }


}
