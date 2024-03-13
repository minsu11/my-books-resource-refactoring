package store.mybooks.resource.user.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.exception.ValidationFailException;
import store.mybooks.resource.user.dto.request.*;
import store.mybooks.resource.user.dto.response.*;
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
            @Valid @RequestBody UserCreateRequest createRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }

        UserCreateResponse createResponse = userService.createUser(createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }


    @PostMapping("/oauth/login")
    public ResponseEntity<UserLoginResponse> loginOauthUser(
            @Valid @RequestBody UserOauthLoginRequest loginRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }

        UserLoginResponse loginResponse = userService.loginOauthUser(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/oauth")
    public ResponseEntity<UserCreateResponse> createOauthUser(
            @Valid @RequestBody UserOauthCreateRequest createRequest) {

        UserCreateResponse createResponse = userService.createOauthUser(createRequest);
        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    /**
     * methodName : modifyUser
     * author : masiljangajji
     * description : 유저의 정보를 변경함 (이름,전화번호)
     *
     * @param id            id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping
    public ResponseEntity<UserModifyResponse> modifyUser(@RequestHeader(name = HeaderProperties.USER_ID) Long id,
                                                         @Valid @RequestBody UserModifyRequest modifyRequest,
                                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }
        UserModifyResponse modifyResponse = userService.modifyUser(id, modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * methodName : modifyUserGrade
     * author : masiljangajji
     * description : 유저의 등급을 변경함
     *
     * @param id            id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{id}/grade")
    public ResponseEntity<UserGradeModifyResponse> modifyUserGrade(@PathVariable(name = "id") Long id,
                                                                   @Valid @RequestBody
                                                                   UserGradeModifyRequest modifyRequest,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }
        UserGradeModifyResponse modifyResponse = userService.modifyUserGrade(id, modifyRequest);
        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * methodName : modifyUserStatus
     * author : masiljangajji
     * description : 유저의 상태를 변경
     *
     * @param id            id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<UserStatusModifyResponse> modifyUserStatus(@PathVariable(name = "id") Long id,
                                                                     @Valid @RequestBody
                                                                     UserStatusModifyRequest modifyRequest,
                                                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }
        UserStatusModifyResponse modifyResponse = userService.modifyUserStatus(id, modifyRequest);
        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * methodName : modifyUserPassword
     * author : masiljangajji
     * description : 유저의 비밀번호를 변경
     *
     * @param id            id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/password")
    public ResponseEntity<UserPasswordModifyResponse> modifyUserPassword(
            @RequestHeader(name = HeaderProperties.USER_ID) Long id,
            @Valid @RequestBody UserPasswordModifyRequest modifyRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }

        UserPasswordModifyResponse modifyResponse = userService.modifyUserPassword(id, modifyRequest);
        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }


    /**
     * methodName : deleteUser
     * author : masiljangajji
     * description : 유저를 삭제함 (강삭제가 아닌 약삭제로 상태를 탈퇴로 변경)
     *
     * @param id id
     * @return response entity
     */
    @DeleteMapping
    public ResponseEntity<UserDeleteResponse> deleteUser(@RequestHeader(name = HeaderProperties.USER_ID) Long id) {
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
    @GetMapping
    public ResponseEntity<UserGetResponse> findUserById(@RequestHeader(name = HeaderProperties.USER_ID) Long id) {

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
    @GetMapping("/all")
    public ResponseEntity<Page<UserGetResponse>> findAllUser(Pageable pageable) {

        Page<UserGetResponse> paginationUsr = userService.findAllUser(pageable);
        return new ResponseEntity<>(paginationUsr, HttpStatus.OK);
    }

    /**
     * methodName : loginUser
     * author : masiljangajji
     * description : 유저의 로그인을 처리함
     *
     * @return response entity
     */
    @PostMapping("/verification")
    public ResponseEntity<UserEncryptedPasswordResponse> verifyUserStatus(
            @Valid @RequestBody UserEmailRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }
        UserEncryptedPasswordResponse userEncryptedPasswordResponse = userService.verifyUserStatusByEmail(request);
        return new ResponseEntity<>(userEncryptedPasswordResponse, HttpStatus.OK);
    }

    @PostMapping("/verification/complete")
    public ResponseEntity<UserLoginResponse> completeLoginProcess(@Valid @RequestBody UserEmailRequest request,
                                                                  BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }
        UserLoginResponse userLoginResponse = userService.completeLoginProcess(request);
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }

    @PostMapping("/verification/dormancy")
    public ResponseEntity<UserInactiveVerificationResponse> dormancyUserVerification(
            @RequestHeader(name = HeaderProperties.USER_ID) Long id) {

        UserInactiveVerificationResponse response = userService.verifyDormancyUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verification/lock")
    public ResponseEntity<UserInactiveVerificationResponse> lockUserVerification(
            @RequestHeader(name = HeaderProperties.USER_ID) Long id,
            @Valid @RequestBody UserPasswordModifyRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailException(bindingResult);
        }
        System.out.println(request.getPassword());
        UserInactiveVerificationResponse response = userService.verifyLockUser(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
