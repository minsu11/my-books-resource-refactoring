package store.mybooks.resource.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.service.UserService;

/**
 * packageName    : store.mybooks.resource.user.controller
 * fileName       : UserController
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody UserCreateRequest createRequest) {


        UserCreateResponse user = userService.createUser(createRequest);


        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
