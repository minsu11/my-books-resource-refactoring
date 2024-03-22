package store.mybooks.resource.user.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.Utils;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserEmailRequest;
import store.mybooks.resource.user.dto.request.UserGradeModifyRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.request.UserOauthCreateRequest;
import store.mybooks.resource.user.dto.request.UserOauthLoginRequest;
import store.mybooks.resource.user.dto.request.UserOauthRequest;
import store.mybooks.resource.user.dto.request.UserPasswordModifyRequest;
import store.mybooks.resource.user.dto.request.UserStatusModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserEncryptedPasswordResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserInactiveVerificationResponse;
import store.mybooks.resource.user.dto.response.UserLoginResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserOauthCreateResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {


    private final UserService userService;

    private final PointHistoryService pointHistoryService;



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

        Utils.validateRequest(bindingResult);

        UserCreateResponse createResponse = userService.createUser(createRequest);
        pointHistoryService.saveSignUpPoint(createResponse.getEmail());
        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }


    @PostMapping("/oauth/login")
    public ResponseEntity<UserLoginResponse> loginOauthUser(
            @Valid @RequestBody UserOauthLoginRequest loginRequest, BindingResult bindingResult) {

        Utils.validateRequest(bindingResult);
        UserLoginResponse loginResponse = userService.loginOauthUser(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/oauth")
    public ResponseEntity<UserOauthCreateResponse> createOauthUser(
            @Valid @RequestBody UserOauthCreateRequest createRequest,BindingResult bindingResult) {

        Utils.validateRequest(bindingResult);
        UserOauthCreateResponse createResponse = userService.createOauthUser(createRequest);
        pointHistoryService.saveSignUpPoint(createResponse.getEmail());

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    @PostMapping("/oauth/no-info")
    public ResponseEntity<UserOauthCreateResponse> createAndLoginOauthUser(@Valid @RequestBody UserOauthRequest request,BindingResult bindingResult){

        Utils.validateRequest(bindingResult);
        UserOauthCreateResponse createResponse = userService.createOauthUser(request);
        pointHistoryService.saveSignUpPoint(createResponse.getEmail());
        pointHistoryService.saveOauthLoginPoint(createResponse.getId());
        return new ResponseEntity<>(createResponse,HttpStatus.CREATED);
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

        Utils.validateRequest(bindingResult);

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
        Utils.validateRequest(bindingResult);

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

        Utils.validateRequest(bindingResult);

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

        Utils.validateRequest(bindingResult);


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
    @GetMapping("/page")
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
        Utils.validateRequest(bindingResult);

        UserEncryptedPasswordResponse userEncryptedPasswordResponse = userService.verifyUserStatusByEmail(request);
        return new ResponseEntity<>(userEncryptedPasswordResponse, HttpStatus.OK);
    }

    @PostMapping("/verification/complete")
    public ResponseEntity<UserLoginResponse> completeLoginProcess(@Valid @RequestBody UserEmailRequest request,
                                                                  BindingResult bindingResult
    ) {
        Utils.validateRequest(bindingResult);

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

        Utils.validateRequest(bindingResult);

        UserInactiveVerificationResponse response = userService.verifyLockUser(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
