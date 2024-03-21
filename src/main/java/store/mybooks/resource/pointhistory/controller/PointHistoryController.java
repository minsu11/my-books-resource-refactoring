package store.mybooks.resource.pointhistory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.pointhistory.dto.request.PointHistoryCreateRequest;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryCreateResponse;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.service.PointHistoryService;

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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/point-histories")
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
    @GetMapping("/points")
    public ResponseEntity<PointResponse> getRemainPoint(@RequestHeader(name = HeaderProperties.USER_ID) Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointHistoryService.getRemainingPoint(userId));
    }

    /**
     * methodName : getPointHistory<br>
     * author : minsu11<br>
     * description : 회원의 포인트 내역.
     * <br> *
     *
     * @param userId 유저아이디
     * @return response entity
     */
    @GetMapping("/history")
    public ResponseEntity<Page<PointHistoryResponse>> getPointHistory(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pointHistoryService.getPointHistory(pageable, userId));
    }


    /**
     * methodName : createPointHistory<br>
     * author : minsu11<br>
     * description : 회원의 포인트 내역 생성.
     * <br> *
     *
     * @param userId  the user id
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<PointHistoryCreateResponse> createPointHistory(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @RequestBody PointHistoryCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pointHistoryService.createPointHistory(request, userId));
    }

    /**
     * methodName : saveLoginPoint <br>
     * author : damho-lee <br>
     * description : 로그인 포인트 적립.<br>
     *
     * @param userId 회원 아이디
     * @return ResponseEntity
     */
    @PostMapping("/login-point")
    public ResponseEntity<Boolean> saveLoginPoint(@RequestHeader(name = HeaderProperties.USER_ID) Long userId) {
        HttpStatus status = pointHistoryService.saveLoginPoint(userId) ? HttpStatus.CREATED : HttpStatus.OK;

        return ResponseEntity
                .status(status)
                .build();
    }

    /**
     * methodName : saveSignUpPoint <br>
     * author : damho-lee <br>
     * description : 회원가입 포인트 적립.<br>
     *
     * @param userId 회원 아이디
     * @return ResponseEntity
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Boolean> saveSignUpPoint(@RequestHeader(name = HeaderProperties.USER_ID) Long userId) {
        pointHistoryService.saveSignUpPoint(userId);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}