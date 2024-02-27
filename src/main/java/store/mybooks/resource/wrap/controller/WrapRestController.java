package store.mybooks.resource.wrap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.service.WrapService;

/**
 * packageName    : store.mybooks.resource.wrap.controller<br>
 * fileName       : WrapRestController<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/wraps")
public class WrapRestController {
    private final WrapService wrapService;

    /**
     * methodName : getWrapResponse<br>
     * author : minsu11<br>
     * description : id로 포장지 조회
     * <br> *
     *
     * @param id 조회할 포장지의 {@code id}
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<WrapResponse> getWrapResponse(@PathVariable Integer id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wrapService.getWrapById(id));
    }


}
