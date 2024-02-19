package store.mybooks.resource.user_status.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user_status.dto.request.UserStatusCreateRequest;
import store.mybooks.resource.user_status.dto.response.UserStatusCreateResponse;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.service.UserStatusService;

/**
 * packageName    : store.mybooks.resource.user_status.controller
 * fileName       : UserStatusRestController
 * author         : masiljangajji
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        masiljangajji       최초 생성
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/statuses")
public class UserStatusRestController {

    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserStatusCreateResponse> createUserStatus(
            @RequestBody UserStatusCreateRequest createRequest) {

        UserStatusCreateResponse createResponse = userStatusService.createUserStatus(createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStatusGetResponse> getUserStatus(@PathVariable(name = "id") String id) {

        UserStatusGetResponse getResponse = userStatusService.findUserStatusById(id);
        return new ResponseEntity<>(getResponse,HttpStatus.OK);
    }


}
