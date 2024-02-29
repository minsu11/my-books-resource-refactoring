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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserGradeModifyRequest;
import store.mybooks.resource.user.dto.request.UserLoginRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.request.UserPasswordModifyRequest;
import store.mybooks.resource.user.dto.request.UserStatusModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserLoginResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserPasswordModifyResponse;
import store.mybooks.resource.user.dto.response.UserStatusModifyResponse;
import store.mybooks.resource.user.service.UserService;

/**
 * packageName    : store.mybooks.resource.user.controller<br>
 * fileName       : UserController<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
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
     * methodName : createUser
     * author : masiljangajji
     * description : 유저를 생성함
     *
     * @param createRequest request
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody UserCreateRequest createRequest) {


        UserCreateResponse createResponse = userService.createUser(createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    /**
     * methodName : modifyUser
     * author : masiljangajji
     * description : 유저의 정보를 변경함 (이름,전화번호)
     *
     * @param id     id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserModifyResponse> modifyUser(@PathVariable(name = "id") Long id,
                                                         @RequestBody UserModifyRequest modifyRequest) {

        UserModifyResponse modifyResponse = userService.modifyUser(id, modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * methodName : modifyUserGrade
     * author : masiljangajji
     * description : 유저의 등급을 변경함
     *
     * @param id id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{id}/grade")
    public ResponseEntity<UserGradeModifyResponse> modifyUserGrade(@PathVariable(name="id")Long id,
                                                                   @RequestBody UserGradeModifyRequest modifyRequest){

        UserGradeModifyResponse modifyResponse = userService.modifyUserGrade(id,modifyRequest);
        return new ResponseEntity<>(modifyResponse,HttpStatus.OK);
    }

    /**
     * methodName : modifyUserStatus
     * author : masiljangajji
     * description : 유저의 상태를 변경
     *
     * @param id id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<UserStatusModifyResponse> modifyUserStatus(@PathVariable(name="id")Long id,
                                                                    @RequestBody UserStatusModifyRequest modifyRequest){

        UserStatusModifyResponse modifyResponse = userService.modifyUserStatus(id,modifyRequest);
        return new ResponseEntity<>(modifyResponse,HttpStatus.OK);
    }

    /**
     * methodName : modifyUserPassword
     * author : masiljangajji
     * description : 유저의 비밀번호를 변경
     *
     * @param id id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<UserPasswordModifyResponse> modifyUserPassword(@PathVariable(name="id")Long id,
                                                                       @RequestBody
                                                                       UserPasswordModifyRequest modifyRequest){

        UserPasswordModifyResponse modifyResponse = userService.modifyUserPassword(id,modifyRequest);
        return new ResponseEntity<>(modifyResponse,HttpStatus.OK);
    }


    /**
     * methodName : deleteUser
     * author : masiljangajji
     * description : 유저를 삭제함 (강삭제가 아닌 약삭제로 상태를 탈퇴로 변경)
     *
     * @param id id
     * @return response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable(name = "id") Long id) {
        UserDeleteResponse deleteResponse = userService.deleteUser(id);

        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }


    /**
     * methodName : findUserById
     * author : masiljangajji
     * description : 유저의 정보를 찾음
     *
     * @param id id
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findUserById(@PathVariable(name = "id") Long id
    , @RequestHeader(name = "ddd",required = false)Long dd) {

        System.out.println(dd);
        UserGetResponse getResponse = userService.findById(id);

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    /**
     * methodName : findAllUser
     * author : masiljangajji
     * description : 모든 유저를 Pagination 해서 찾음
     *
     * @param pageable pageable
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<Page<UserGetResponse>> findAllUser(Pageable pageable) {

        Page<UserGetResponse> paginationUsr = userService.findAllUser(pageable);
        return new ResponseEntity<>(paginationUsr, HttpStatus.OK);
    }

    /**
     * methodName : loginUser
     * author : masiljangajji
     * description : 유저의 로그인을 처리함
     *
     * @param userLoginRequest login request
     * @return response entity
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {

        UserLoginResponse userLoginResponse = userService.loginUser(userLoginRequest);
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }


}
