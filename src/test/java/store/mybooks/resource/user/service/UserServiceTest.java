package store.mybooks.resource.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.hibernate.validator.constraints.time.DurationMax;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import store.mybooks.resource.user.dto.mapper.UserMapper;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserGradeModifyRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.request.UserPasswordModifyRequest;
import store.mybooks.resource.user.dto.request.UserStatusModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserAlreadyExistException;
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

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("이미 사용중인 email 을 담고있는 UserCreateRequest 를 이용해 CreateUser 실행시 UserAlreadyExistException")
    void givenUserCreateRequestWithAlreadyUsedEmail_whenCallCreateUser_thenThrowUserAlreadyExistException(
            @Mock UserCreateRequest userCreateRequest,
            @Mock User user) {

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
        userService.modifyUserPassword(1L,request);

        verify(userRepository,times(1)).findById(anyLong());
        verify(user,times(1)).modifyPassword(anyString());

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


}