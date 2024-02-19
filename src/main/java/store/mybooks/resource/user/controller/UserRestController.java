package store.mybooks.resource.user.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
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
public class UserRestController {


    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody UserCreateRequest createRequest) {


        UserCreateResponse createResponse = userService.createUser(createRequest);


        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserModifyResponse> modifyUser(@PathVariable(name = "email") String email,
                                                         @RequestBody UserModifyRequest modifyRequest) {

        UserModifyResponse modifyResponse = userService.modifyUser(email,modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable(name = "email") String email) {
        UserDeleteResponse deleteResponse = userService.deleteUser(email);

        return new ResponseEntity<>(deleteResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserGetResponse> findUserByEmail(@PathVariable(name = "email") String email) {


        UserGetResponse getResponse = userService.findByEmail(email);

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserGetResponse>> findAllUser(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer size) {

        Page<UserGetResponse> paginationUsr = userService.findAllUser(page, size);
        return new ResponseEntity<>(paginationUsr,HttpStatus.OK);
    }


}
