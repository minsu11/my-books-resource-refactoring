package store.mybooks.resource.point_history.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.point_history.dto.response.PointResponse;
import store.mybooks.resource.point_history.service.PointHistoryService;

/**
 * packageName    : store.mybooks.resource.point_history.controller<br>
 * fileName       : PointHistoryController<br>
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
@RestController("/api/point-histories")
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    /**
     * methodName : getRemainPoint<br>
     * author : minsu11<br>
     * description : 회원의 잔여 포인트 조회
     * <br> *
     *
     * @param userId 유저아이디
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<PointResponse> getRemainPoint(@RequestHeader(name = HeaderProperties.USER_ID) Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointHistoryService.getRemainingPoint(userId));
    }
}
