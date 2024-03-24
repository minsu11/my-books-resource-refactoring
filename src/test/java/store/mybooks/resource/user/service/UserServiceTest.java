package store.mybooks.resource.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.user.dto.mapper.UserMapper;
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
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserOauthCreateResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserAlreadyExistException;
import store.mybooks.resource.user.exception.UserAlreadyResignException;
import store.mybooks.resource.user.exception.UserLoginFailException;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.exception.UserGradeIdNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user.service
 * fileName       : UserServiceTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @Mock
    UserGradeRepository userGradeRepository;

    @Mock
    PointHistoryService pointHistoryService;

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;


    @Test
    @DisplayName("이미 사용중인 email 을 담고있는 UserCreateRequest 를 이용해 CreateUser 실행시 UserAlreadyExistException")
    void givenUserCreateRequestWithAlreadyUsedEmail_whenCallCreateUser_thenThrowUserAlreadyExistException(
            @Mock UserCreateRequest userCreateRequest) {

        when(userCreateRequest.getEmail()).thenReturn("test");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userCreateRequest));
    }

    @Test
    @DisplayName("UserCreateRequest 를 이용해 CreateUser 실행시 동작 테스트")
    void givenUserCreateRequest_whenCallCreateUser_thenReturnUserCreateResponse(
            @Mock UserCreateRequest userCreateRequest,
            @Mock UserStatus userStatus,
            @Mock UserGrade userGrade,
            @Mock UserCreateResponse userCreateResponse) {

        when(userCreateRequest.getEmail()).thenReturn("test");
        when(userCreateRequest.getPassword()).thenReturn("test");
        when(userCreateRequest.getBirth()).thenReturn(LocalDate.parse("1000-01-01"));
        when(userRepository.existsByEmail("test")).thenReturn(false);
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.of(userStatus));
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.of(userGrade));
        when(userMapper.toUserCreateResponse(any(User.class))).thenReturn(userCreateResponse);
        userService.createUser(userCreateRequest);

        verify(userStatusRepository, times(1)).findById(anyString());
        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 UserStatusName 을 담고있는 UserCreateRequest 를 이용해 CreateUser 실행시 UserStatusNotExistException")
    void givenUserCreateRequestWithNotExistUserStatusName_whenCallCreateUser_thenThrowUserStatusNotExistException(
            @Mock UserCreateRequest userCreateRequest) {

        when(userCreateRequest.getEmail()).thenReturn("test");
        when(userRepository.existsByEmail("test")).thenReturn(false);

        assertThrows(UserStatusNotExistException.class, () -> userService.createUser(userCreateRequest));

    }

    @Test
    @DisplayName("존재하지 않는 UserGradeName 을 담고있는 UserCreateRequest 를 이용해 CreateUser 실행시 UserGradeNameNotExistException")
    void givenUserCreateRequestWithNotExistUserGradeName_whenCallCreateUser_thenThrowUserGradeNameNotExistException(
            @Mock UserCreateRequest userCreateRequest,
            @Mock UserStatus userStatus) {

        when(userCreateRequest.getEmail()).thenReturn("test");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.of(userStatus));

        assertThrows(UserGradeNameNotExistException.class, () -> userService.createUser(userCreateRequest));
    }

    @Test
    @DisplayName("UserId 와 UserGradeModifyRequest 를 이용해 ModifyUser 실행시 동작 테스트")
    void givenUserIdAndUserModifyRequest_whenCallModifyUserGrade_thenReturnUserModifyResponse(
            @Mock UserGradeModifyRequest modifyRequest,
            @Mock User user,
            @Mock UserGrade userGrade
    ) {

        when(modifyRequest.getUserGradeName()).thenReturn("test");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.of(userGrade));

        userService.modifyUserGrade(1L, modifyRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 UserId 와 UserModifyRequest 를 이용해 ModifyUser 실행시 동작테스트")
    void givenNotExistUserId_whenCallModifyUser_thenThrowUserNotExistException(@Mock UserModifyRequest modifyRequest) {

        assertThrows(UserNotExistException.class, () -> userService.modifyUser(1L, modifyRequest));
    }

    @Test
    @DisplayName("ModifyUser 실행시 동작테스트")
    void givenUserModifyRequest_whenCallModifyUser_thenReturnUserModifyResponse(@Mock UserModifyRequest modifyRequest, @Mock User user, @Mock
                                                                                UserModifyResponse userModifyResponse) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(modifyRequest.getName()).thenReturn("modify name");
        when(modifyRequest.getPhoneNumber()).thenReturn("modify phone");
        when(userMapper.toUserModifyResponse(user)).thenReturn(userModifyResponse);

        userService.modifyUser(1L,modifyRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(modifyRequest, times(1)).getName();
        verify(modifyRequest, times(1)).getPhoneNumber();
        verify(user,times(1)).modifyUser(anyString(),anyString());

    }

    @Test
    @DisplayName("회원가입시 유저 이메일 확인")
    void givenUserEmailRequest_whenCallVerifyUserEmail_thenReturnUserEmailCheckResponse(@Mock UserEmailRequest request) {

        when(request.getEmail()).thenReturn("email@test.com");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        userService.verifyUserEmail(request);

        verify(request, times(1)).getEmail();
        verify(userRepository, times(1)).existsByEmail(anyString());

    }

    @Test
    @DisplayName("존재하는 UserStatusName 을 담고있는 UserStatusModifyRequest 를 이용해 modifyUserStatus 실행시 UserStatusNotExistException")
    void givenStatusModifyRequestWithExistUserStatusName_whenCallModifyUserStatus_thenThrowUserStatusNotExistException(
            @Mock UserStatusModifyRequest modifyRequest,
            @Mock User user,
            @Mock UserStatus userStatus
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.of(userStatus));
        when(modifyRequest.getUserStatusName()).thenReturn("test");
        userService.modifyUserStatus(1L, modifyRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userStatusRepository, times(1)).findById(anyString());

    }

    @Test
    @DisplayName("존재하지 않는 UserStatusName 을 담고있는 UserStatusModifyRequest 를 이용해 modifyUserStatus 실행시 UserStatusNotExistException")
    void givenStatusModifyRequestWithNotExistUserStatusName_whenCallModifyUserStatus_thenThrowUserStatusNotExistException(
            @Mock UserStatusModifyRequest modifyRequest,
            @Mock User user
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(UserStatusNotExistException.class, () -> userService.modifyUserStatus(1L, modifyRequest));
    }

    @Test
    @DisplayName("존재하지 않는 UserGradeName 을 담고있는 UserGradeModifyRequest 를 이용해 modifyUserGrade 실행시 UserGradeNotExistException")
    void givenGradeModifyRequestWithNotExistUserGradeName_whenCallModifyUserGrade_thenThrowUserGradeNotExistException(
            @Mock UserGradeModifyRequest modifyRequest,
            @Mock User user
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(UserGradeIdNotExistException.class, () -> userService.modifyUserGrade(1L, modifyRequest));
    }

    @Test
    @DisplayName("UserPasswordModifyRequest 를 이용해 modifyUserPassword 실행시 동작 테스트")
    void givenUserPasswordModifyRequest_whenCallModifyUserPassword_thenReturnUserPasswordModifyResponse(
            @Mock UserPasswordModifyRequest request,
            @Mock User user) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(request.getPassword()).thenReturn("test");
        userService.modifyUserPassword(1L, request);

        verify(userRepository, times(1)).findById(anyLong());
        verify(user, times(1)).modifyPassword(anyString());

    }


    @Test
    @DisplayName("존재하는 UserGradeName 을 담고있는 UserGradeModifyRequest 를 이용해 modifyUserGrade 실행시 UserGradeNotExistException")
    void givenGradeModifyRequestWithExistUserGradeName_whenCallModifyUserGrade_thenThrowUserGradeNotExistException(
            @Mock UserGradeModifyRequest modifyRequest,
            @Mock User user,
            @Mock UserGrade userGrade
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(modifyRequest.getUserGradeName()).thenReturn("test");
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.of(userGrade));

        userService.modifyUserGrade(1L, modifyRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(anyString());

    }

    @Test
    @DisplayName("UserId 를 이용해 DeleteUser 실행시 동작 테스트")
    void givenUserId_whenCallDeleteUser_thenReturnUserDeleteResponse(
            @Mock User user,
            @Mock UserStatus userStatus
    ) {


        when(user.getEmail()).thenReturn("This_Is_Test_Email");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.of(userStatus));

        UserDeleteResponse userDeleteResponse = userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userStatusRepository, times(1)).findById(anyString());

        assertTrue(userDeleteResponse.getMessage().contains("This_Is_Test_Email"));
    }

    @Test
    @DisplayName("존재하지 않는 UserStatusName 으로 DeleteUser 실행시 UserStatusNotExistException")
    void givenUserIdWithNotExistUserStatusName_whenCallDeleteUser_thenThrowUserStatusNotExistException(
            @Mock User user
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(UserStatusNotExistException.class, () -> userService.deleteUser(1L));
    }


    @Test
    @DisplayName("존재하지 않는 UserId 를 이용해 findById 실행시 UserNotExistException")
    void givenNotExistUserId_whenCallFindById_thenThrowUserNotExistException() {
        assertThrows(UserNotExistException.class, () -> userService.findById(1L));
    }

    @Test
    @DisplayName("UserId 를 이용해 findById 실행시 동작 테스트")
    void givenUserId_whenCallFindById_thenReturnUserGetResponse(@Mock UserGetResponse userGetResponse) {

        when(userRepository.queryById(anyLong())).thenReturn(Optional.of(userGetResponse));
        userService.findById(1L);
        verify(userRepository, times(1)).queryById(anyLong());
    }

    @Test
    @DisplayName("Pageable 을 이용해 findAllUsr 실행시 동작 테스트")
    void givenPageable_whenCallFindAllUSer_thenReturnUserGetResponsePage(@Mock Page page) {

        when(userRepository.queryAllBy(any())).thenReturn(page);

        userService.findAllUser(any());

        verify(userRepository, times(1)).queryAllBy(any());
    }

    @Test
    @DisplayName("UserOauthCreateRequest 를 이용해 createOauthUser 실행시 동작 테스트")
    void givenUserOauthCreateRequest_whenCallCreateOauthUser_thenReturnUserCreateResponse(
            @Mock UserOauthCreateRequest userOauthCreateRequest
            , @Mock UserStatus userStatus, @Mock UserGrade userGrade) {

        when(userStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(userStatus));

        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.ofNullable(userGrade));

        when(userOauthCreateRequest.getEmail()).thenReturn("test@test.com");
        when(userOauthCreateRequest.getBirthMonthDay()).thenReturn("1217");
        when(userOauthCreateRequest.getPhoneNumber()).thenReturn("01012345678");
        when(userOauthCreateRequest.getName()).thenReturn("test");

        userService.createOauthUser(userOauthCreateRequest);

        verify(userStatusRepository, times(1)).findById(anyString());
        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(anyString());
    }

    @Test
    @DisplayName("존재하지 않는 UserStatus 를 이용해 createOauthUser 실행시 UserStatusNotExistException")
    void givenNotExistUserStatus_whenCallCreateOauthUser_thenThrowUserStatusNotExistException(
            @Mock UserOauthCreateRequest userOauthCreateRequest) {
        assertThrows(UserStatusNotExistException.class, () -> userService.createOauthUser(userOauthCreateRequest));
    }

    @Test
    @DisplayName("존재하지 않는 UserGrade 를 이용해 createOauthUser 실행시 UserGradeNameNotExistException")
    void givenNotExistUserGrade_whenCallCreateOauthUser_thenThrowUserGradeNameNotExistException(
            @Mock UserOauthCreateRequest userOauthCreateRequest, @Mock UserStatus userStatus) {
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(userStatus));

        assertThrows(UserGradeNameNotExistException.class, () -> userService.createOauthUser(userOauthCreateRequest));
    }


    @Test
    @DisplayName("UserEmailRequest 를 이용해 completeLoginProcess 실행시 동작 테스트")
    void givenUserEmailRequest_whenCallCompleteLoginProcess_thenReturnUserLoginResponse(@Mock User user, @Mock
    UserEmailRequest userEmailRequest, @Mock UserStatus userStatus) {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(userEmailRequest.getEmail()).thenReturn("test");
        assert user != null;
        when(user.getIsAdmin()).thenReturn(true);
        when(user.getId()).thenReturn(1L);
        when(user.getUserStatus()).thenReturn(userStatus);
        when(userStatus.getId()).thenReturn("test");
        when(pointHistoryService.saveLoginPoint(user.getId())).thenReturn(true);
        userService.completeLoginProcess(userEmailRequest);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(user, times(1)).modifyLatestLogin();
        verify(pointHistoryService, times(1)).saveLoginPoint(user.getId());
    }

    @Test
    @DisplayName("존재하지않는 User Email 을 이용해 completeLoginProcess 실행시 UserLoginFailException")
    void givenUserEmailRequestWithNotExist_whenCallCompleteLoginProcess_thenThrowUserLoginResponse(@Mock
                                                                                                   UserEmailRequest userEmailRequest) {

        assertThrows(UserLoginFailException.class, () -> userService.completeLoginProcess(userEmailRequest));

    }


    @Test
    @DisplayName("UserEmailRequest 를 이용해 verifyUserStatusByEmail 실행시 동작 테스트")
    void givenUserEmailRequest_whenCallVerifyUserStatusByEmail_thenReturnUserLoginResponse(@Mock User user, @Mock
    UserEmailRequest userEmailRequest, @Mock UserStatus userStatus) {

        when(userEmailRequest.getEmail()).thenReturn("test");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        assert user != null;
        when(user.getPassword()).thenReturn("password");
        when(user.getUserStatus()).thenReturn(userStatus);
        when(userStatus.getId()).thenReturn("test");

        userService.verifyUserStatusByEmail(userEmailRequest);

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("존재하지않는 User Email 을 이용해 verifyUserStatusByEmail 실행시 UserLoginFailException")
    void givenUserEmailRequestWithNotExist_whenCallVerifyUserStatusByEmail_thenThrowUserLoginFailException(@Mock
                                                                                                           UserEmailRequest userEmailRequest) {

        assertThrows(UserLoginFailException.class, () -> userService.verifyUserStatusByEmail(userEmailRequest));
    }

    @Test
    @DisplayName("탈퇴한 User Email 을 이용해 verifyUserStatusByEmail 실행시 UserAlreadyResignException")
    void givenUserEmailRequestWithResign_whenCallVerifyUserStatusByEmail_thenThrowUserAlreadyResignException(
            @Mock User user, @Mock
    UserEmailRequest userEmailRequest, @Mock UserStatus userStatus) {

        when(userEmailRequest.getEmail()).thenReturn("test");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        assert user != null;
        when(user.getUserStatus()).thenReturn(userStatus);
        when(userStatus.getId()).thenReturn("탈퇴");
        assertThrows(UserAlreadyResignException.class, () -> userService.verifyUserStatusByEmail(userEmailRequest));

    }


    @Test
    @DisplayName("UserId 를 이용해 verifyDormancyUser 실행시 동작 테스트")
    void givenUserId_whenCallVerifyDormancyUser_thenReturnUserInactiveVerificationResponse(@Mock User user, @Mock
    UserStatus userStatus) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(userStatus));

        userService.verifyDormancyUser(1L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userStatusRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("존재하지않는 UserId 를 이용해 verifyDormancyUser 실행시 UserNotExistException")
    void givenNotExistUserId_whenCallVerifyDormancyUser_thenThrowUserNotExistException() {

        assertThrows(UserNotExistException.class, () -> userService.verifyDormancyUser(anyLong()));
    }

    @Test
    @DisplayName("User 활성상태가 존재하지 않는 경우 verifyDormancyUser 실행시 UserStatusNotExistException")
    void givenUserId_whenCallVerifyDormancyUser_thenThrowUserStatusNotExistException(@Mock User user) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        assertThrows(UserStatusNotExistException.class, () -> userService.verifyDormancyUser(anyLong()));
    }

    @Test
    @DisplayName("UserId 와 UserPasswordModifyRequest 를 이용해 verifyLockUser 실행시 동작 테스트")
    void givenUserIdAndUserPasswordModifyRequest_whenCallVerifyLockUser_thenReturnUserInactiveVerificationResponse(
            @Mock User user, @Mock
    UserStatus userStatus, @Mock UserPasswordModifyRequest userPasswordModifyRequest) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(userStatus));
        when(userPasswordModifyRequest.getPassword()).thenReturn("password");
        doNothing().when(user).modifyUserStatus(any(UserStatus.class));
        doNothing().when(user).modifyPassword(anyString());

        userService.verifyLockUser(1L, userPasswordModifyRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userStatusRepository, times(1)).findById(anyString());
        verify(user, times(1)).modifyUserStatus(any(UserStatus.class));
        verify(user, times(1)).modifyPassword(anyString());
    }

    @Test
    @DisplayName("존재하지않는 UserId 를 이용해 verifyLockUser 실행시 UserNotExistException")
    void givenNotExistUserId_whenCallVerifyLockUser_thenThrowUserNotExistException(
            @Mock UserPasswordModifyRequest userPasswordModifyRequest) {

        assertThrows(UserNotExistException.class,
                () -> userService.verifyLockUser(anyLong(), userPasswordModifyRequest));
    }

    @Test
    @DisplayName("User 활성상태가 존재하지 않는 경우 verifyLockUser 실행시 UserStatusNotExistException")
    void givenUserId_whenCallVerifyLockUser_thenThrowUserStatusNotExistException(@Mock User user, @Mock
    UserPasswordModifyRequest userPasswordModifyRequest) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        assertThrows(UserStatusNotExistException.class,
                () -> userService.verifyLockUser(anyLong(), userPasswordModifyRequest));
    }

    @Test
    @DisplayName("oauth 유저가 정보제공 동의 한 경우 회원가입 실행")
    void givenCreateOauthUser_whenCallCreateOauthUser_thenReturnUserOauthCreateResponse(@Mock
                                                                                        UserOauthCreateRequest createRequest,
                                                                                        @Mock UserStatus userStatus,
                                                                                        @Mock UserGrade userGrade,
                                                                                        @Mock User mockUser) {


        when(userStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(userStatus));
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.ofNullable(userGrade));

        when(createRequest.getEmail()).thenReturn("email@test.com");
        when(createRequest.getBirthMonthDay()).thenReturn("12-17");
        when(createRequest.getPhoneNumber()).thenReturn("01012345678");
        when(createRequest.getName()).thenReturn("name");
        when(createRequest.getOauthId()).thenReturn("oauthId");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toUserOauthCreateResponse(any(User.class))).thenReturn(any(UserOauthCreateResponse.class));
        userService.createOauthUser(createRequest);


        verify(createRequest, times(1)).getEmail();
        verify(createRequest, times(1)).getBirthMonthDay();
        verify(createRequest, times(1)).getPhoneNumber();
        verify(createRequest, times(1)).getName();
        verify(createRequest, times(1)).getOauthId();
        verify(userStatusRepository, times(1)).findById(anyString());
        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserOauthCreateResponse(any(User.class));

    }

    @Test
    @DisplayName("oauth 유저가 정보제공 비동의 한 경우 회원가입 실행")
    void givenUserRequest_whenCallCreateOauthUser_thenReturnUserOauthCreateResponse(@Mock
                                                                                    UserOauthRequest oauthRequest,
                                                                                    @Mock UserStatus userStatus,
                                                                                    @Mock UserGrade userGrade,
                                                                                    @Mock User mockUser) {

        when(userStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(userStatus));
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.ofNullable(userGrade));

        when(oauthRequest.getEmail()).thenReturn("email@test.com");
        when(oauthRequest.getBirth()).thenReturn(LocalDate.now());
        when(oauthRequest.getPhoneNumber()).thenReturn("01012345678");
        when(oauthRequest.getName()).thenReturn("name");
        when(oauthRequest.getOauthId()).thenReturn("oauthId");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toUserOauthCreateResponse(any(User.class))).thenReturn(any(UserOauthCreateResponse.class));
        userService.createOauthUser(oauthRequest);

        verify(oauthRequest, times(2)).getEmail();
        verify(oauthRequest, times(1)).getBirth();
        verify(oauthRequest, times(1)).getPhoneNumber();
        verify(oauthRequest, times(1)).getName();
        verify(oauthRequest, times(1)).getOauthId();
        verify(userStatusRepository, times(1)).findById(anyString());
        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserOauthCreateResponse(any(User.class));

    }

    @Test
    @DisplayName("oauth 유저 로그인 실행 (이미 회원가입한 회원인 경우)")
    void givenUserOauthLoginRequest_whenCallLoginOauthUser_thenReturnUserLoginResponse(@Mock
                                                                                       UserOauthLoginRequest loginRequest,
                                                                                    @Mock User user,@Mock UserStatus userStatus) {


        when(loginRequest.getOauthId()).thenReturn("oauthID");
        when(userRepository.findByOauthId(anyString())).thenReturn(Optional.ofNullable(user));
        when(user.getIsAdmin()).thenReturn(false);
        when(user.getUserStatus()).thenReturn(userStatus);
        when(userStatus.getId()).thenReturn("활성");
        doNothing().when(user).modifyLatestLogin();;
        when(pointHistoryService.saveLoginPoint(anyLong())).thenReturn(true);
        userService.loginOauthUser(loginRequest);
        verify(loginRequest, times(1)).getOauthId();
        verify(user, times(1)).getIsAdmin();
        verify(user, times(1)).getUserStatus();
        verify(userStatus, times(1)).getId();
        verify(user, times(1)).modifyLatestLogin();
        verify(pointHistoryService, times(1)).saveLoginPoint(anyLong());
        verify(userRepository, times(1)).findByOauthId(anyString());

    }


}