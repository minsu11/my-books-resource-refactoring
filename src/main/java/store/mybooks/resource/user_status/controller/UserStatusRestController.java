package store.mybooks.resource.user_status.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * Gets user status.
     *
     * id를 통해 특정 UserStatus를 조회하는 api
     *
     * @param id the id
     * @return the user status
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserStatusGetResponse> getUserStatus(@PathVariable(name = "id") String id) {

        UserStatusGetResponse getResponse = userStatusService.findUserStatusById(id);
        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }

    /**
     * Gets all user status.
     *
     * 모든 UserStatus를 List형태로 조회하는 api
     *
     * @return the all user status
     */
    @GetMapping
    public ResponseEntity<List<UserStatusGetResponse>> getAllUserStatus() {

        List<UserStatusGetResponse> getResponses = userStatusService.findAllUserStatus();

        return new ResponseEntity<>(getResponses, HttpStatus.OK);
    }


}
