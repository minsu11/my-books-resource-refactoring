package store.mybooks.resource.user.controller;

import java.util.List;
import javax.ws.rs.POST;
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
import store.mybooks.resource.user.dto.request.UserLoginRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserLoginResponse;
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


    /**
     * Create user response entity.
     * <p>
     * User를 생성하는 api
     *
     * @param createRequest the create request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody UserCreateRequest createRequest) {


        UserCreateResponse createResponse = userService.createUser(createRequest);


        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    /**
     * Modify user response entity.
     * <p>
     * id로 찾은 User의 정보를 수정하는 api
     *
     * @param id            the id
     * @param modifyRequest the modify request
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserModifyResponse> modifyUser(@PathVariable(name = "id") Long id,
                                                         @RequestBody UserModifyRequest modifyRequest) {

        UserModifyResponse modifyResponse = userService.modifyUser(id, modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * Delete user response entity.
     * <p>
     * id로 찾은 User를 삭제하는 api
     * 강삭제가 아닌 약삭제를 제공함
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable(name = "id") Long id) {
        UserDeleteResponse deleteResponse = userService.deleteUser(id);

        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    /**
     * Find user by email response entity.
     * <p>
     * id로 찾은 User를 반환함
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findUserByEmail(@PathVariable(name = "id") Long id) {


        UserGetResponse getResponse = userService.findById(id);

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    /**
     * Find all user response entity.
     * 모든 User를 Pagination 해서 반환함
     *
     * @param pageable the pageable
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<Page<UserGetResponse>> findAllUser(Pageable pageable) {

        Page<UserGetResponse> paginationUsr = userService.findAllUser(pageable);
        return new ResponseEntity<>(paginationUsr, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {

        return new ResponseEntity<>(userService.loginUser(userLoginRequest), HttpStatus.OK);
    }


}
