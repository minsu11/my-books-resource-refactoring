package store.mybooks.resource.user_status.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.service.UserStatusService;

/**
 * packageName    : store.mybooks.resource.user_status.controller<br>
 * fileName       : UserStatusRestController<br>
 * author         : masiljangajji<br>
 * date           : 2/18/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        masiljangajji       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users-statuses")
public class UserStatusRestController {

    private final UserStatusService userStatusService;


    /**
     * methodName : getAllUserStatus
     * author : masiljangajji
     * description : 모든 유저상태 정보를 가져옴
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<UserStatusGetResponse>> getAllUserStatus() {

        List<UserStatusGetResponse> getResponses = userStatusService.findAllUserStatus();

        return new ResponseEntity<>(getResponses, HttpStatus.OK);
    }


}
