package store.mybooks.resource.user.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import store.mybooks.resource.user.dto.response.UserEmailCheckResponse;
import store.mybooks.resource.user.dto.response.UserEncryptedPasswordResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserInactiveVerificationResponse;
import store.mybooks.resource.user.dto.response.UserLoginResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserOauthCreateResponse;
import store.mybooks.resource.user.dto.response.UserPasswordModifyResponse;
import store.mybooks.resource.user.dto.response.UserStatusModifyResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserAlreadyExistException;
import store.mybooks.resource.user.exception.UserAlreadyResignException;
import store.mybooks.resource.user.exception.UserLoginFailException;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.exception.UserGradeIdNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.enumeration.UserStatusEnum;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user.service<br>
 * fileName       : UserService<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@AllArgsConstructor
@Service
@Transactional
public class UserService {


    private final UserRepository userRepository;

    private final UserStatusRepository userStatusRepository;

    private final UserGradeRepository userGradeRepository;

    private final UserMapper userMapper;

    private final PointHistoryService pointHistoryService;

    /**
     * methodName : createUser
     * author : masiljangajji
     * description : 회원가입처리
     *
     * @param createRequest 유저의 정보
     * @return user create response
     * @throws UserAlreadyExistException      이미 등록된 이메일인 경우
     * @throws UserStatusNotExistException    유저의 상태가 존재하지 않는 경우
     * @throws UserGradeNameNotExistException 유저의 등급이름이 존재하지 않는 경우
     */
    public UserCreateResponse createUser(UserCreateRequest createRequest) {


        // 이미 존재하면 예외처리
        if (userRepository.existsByEmail(createRequest.getEmail())) {
            throw new UserAlreadyExistException(createRequest.getEmail());
        }

        String userStatusName = UserStatusEnum.ACTIVE.toString();
        String userGradeName = UserGradeNameEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeName));

        User user = new User(createRequest.getEmail(), createRequest.getBirth(),
                createRequest.getPassword(),
                createRequest.getPhoneNumber(), false, createRequest.getName(), userStatus,
                userGrade, null);

        userRepository.save(user);

        return userMapper.toUserCreateResponse(user);
    }

    /**
     * methodName : createOauthUser
     * author : masiljangajji
     * description : 소셜회원 최초 로그인시 회원가입 처리 (정보제공 동의시)
     *
     * @param createRequest 제공 동의된 유저 정보
     * @return user oauth create response
     * @throws UserStatusNotExistException    유저 상태가 존재하지 않는 경우
     * @throws UserGradeNameNotExistException 유저 등급이름이 존재하지 않는 경우
     */
    public UserOauthCreateResponse createOauthUser(UserOauthCreateRequest createRequest) {

        String userStatusName = UserStatusEnum.ACTIVE.toString();
        String userGradeName = UserGradeNameEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeName));

        User user = new User(createRequest.getEmail(), null, createRequest.getBirthMonthDay(), "dummy",
                createRequest.getPhoneNumber(), false, createRequest.getName(), userStatus, userGrade,
                createRequest.getOauthId());

        User resultUser = userRepository.save(user);
        return userMapper.toUserOauthCreateResponse(resultUser);
    }

    /**
     * methodName : createOauthUser
     * author : masiljangajji
     * description : 소셜회원 최초 로그인시 회원가입 처리 (정보제공 바동의시)
     * 추가로 입력받은 정보를 이용해 회원가입 처리
     *
     * @param request 유저에게 입력받은 정보
     * @return user oauth create response
     * @throws UserStatusNotExistException    유저 상태가 존재하지 않는 경우
     * @throws UserGradeNameNotExistException 유저 등급이름이 존재하지 않는 경우
     * @throws UserAlreadyExistException      이미 회원가입한 유저인 경우
     */
    public UserOauthCreateResponse createOauthUser(UserOauthRequest request) {

        String userStatusName = UserStatusEnum.ACTIVE.toString();
        String userGradeName = UserGradeNameEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeName));

        // 이미 존재하면 예외처리
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException(request.getEmail());
        }

        User user = new User(request.getEmail(), request.getBirth(), "dummy", request.getPhoneNumber(), false,
                request.getName(), userStatus, userGrade, request.getOauthId());
        user.modifyLatestLogin();
        User resultUser = userRepository.save(user);
        return userMapper.toUserOauthCreateResponse(resultUser);
    }


    /**
     * methodName : loginOauthUser
     * author : masiljangajji
     * description : 소셜로그인을 처리
     *
     * @param loginRequest oauthId
     * @return user login response
     */
    public UserLoginResponse loginOauthUser(UserOauthLoginRequest loginRequest) {

        Optional<User> user = userRepository.findByOauthId(loginRequest.getOauthId());

        if (user.isPresent()) { // 이미 있으면 = 회원가입한 회원이면
            User existUser = user.get();
            pointHistoryService.saveLoginPoint(existUser.getId());
            existUser.modifyLatestLogin();
            return new UserLoginResponse(true, existUser.getIsAdmin(), existUser.getId(),
                    existUser.getUserStatus().getId()); // 로그인 response 보내기
        }

        return new UserLoginResponse(false, false, null, null);

    }


    /**
     * methodName : modifyUser
     * author : masiljangajji
     * description : 유저의 정보를 변경함 (이름,전화번호)
     *
     * @param id            id
     * @param modifyRequest 이름 , 전화번호
     * @return user modify response
     * @throws UserNotExistException 유저가 존재하지 않는 경우
     */
    public UserModifyResponse modifyUser(Long id, UserModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(id));

        user.modifyUser(modifyRequest.getName(), modifyRequest.getPhoneNumber());
        return userMapper.toUserModifyResponse(user);
    }


    /**
     * methodName : modifyUserGrade
     * author : masiljangajji
     * description : 유저의 등급을 변경함
     *
     * @param id            id
     * @param modifyRequest request
     * @return user grade modify response
     * @throws UserNotExistException        유저가 존재하지 않는 경우
     * @throws UserGradeIdNotExistException 유저의 등급이 존재하지 않는 경우
     */
    public UserGradeModifyResponse modifyUserGrade(Long id, UserGradeModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(id));

        String userGradeName = modifyRequest.getUserGradeName();

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName).orElseThrow(
                UserGradeIdNotExistException::new);
        user.modifyUserGrade(userGrade);

        return userMapper.toUserGradeModifyResponse(user);
    }

    /**
     * methodName : modifyUserStatus
     * author : masiljangajji
     * description : 유저의 상태를 변경함
     *
     * @param id            id
     * @param modifyRequest 유저상태 이름
     * @return user status modify response
     * @throws UserNotExistException       유저가 존재하지 않는 경우
     * @throws UserStatusNotExistException 유저상태가 존재하지 않는 경우
     */
    public UserStatusModifyResponse modifyUserStatus(Long id, UserStatusModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(id));

        String userStatusName = modifyRequest.getUserStatusName();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));
        user.modifyUserStatus(userStatus);
        return userMapper.toUserStatusModifyResponse(user);
    }

    /**
     * methodName : modifyUserPassword
     * author : masiljangajji
     * description : 유저의 비밀번호 변경
     *
     * @param id            id
     * @param modifyRequest 비밀번호
     * @return user password modify response
     * @throws UserNotExistException 유저가 존재하지 않는 경우
     */
    public UserPasswordModifyResponse modifyUserPassword(Long id, UserPasswordModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(id));

        user.modifyPassword(modifyRequest.getPassword());
        return new UserPasswordModifyResponse(true);
    }

    /**
     * methodName : deleteUser
     * author : masiljangajji
     * description :유저 삭제 (강삭제가 아닌 약삭제로 탈퇴시 상태가 탈퇴로 변경됨)
     *
     * @param id id
     * @return user delete response
     * @throws UserStatusNotExistException 유저의 상태가 존재하지 않는 경우
     */
    public UserDeleteResponse deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException(id));

        String userStatusName = UserStatusEnum.RESIGN.toString();
        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        user.modifyByDeleteRequest(userStatus);
        return new UserDeleteResponse(String.format("[%s] 유저 삭제완료", user.getEmail()));
    }

    /**
     * methodName : findById
     * author : masiljangajji
     * description :유저의 정보를 찾음
     *
     * @param id id
     * @return user get response
     * @throws UserNotExistException 유저가 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public UserGetResponse findById(Long id) {
        return userRepository.queryById(id).orElseThrow(() -> new UserNotExistException(id));
    }


    /**
     * methodName : findAllUser
     * author : masiljangajji
     * description : 모든 유저를 찾아 Pagination 함
     *
     * @param pageable pageable
     * @return page
     */
    @Transactional(readOnly = true)
    public Page<UserGetResponse> findAllUser(Pageable pageable) {

        return userRepository.queryAllBy(pageable);
    }


    /**
     * methodName : completeLoginProcess
     * author : masiljangajji
     * description : 로그인 완료를 처리함
     * 로그인 포인트 적립 및 마지막 로그인 시간을 갱신
     *
     * @param request 이메일
     * @return user login response
     * @throws UserLoginFailException 이메일이 존재하지 않는 경우
     */
    public UserLoginResponse completeLoginProcess(UserEmailRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserLoginFailException::new);

        pointHistoryService.saveLoginPoint(user.getId());
        user.modifyLatestLogin();

        return new UserLoginResponse(true, user.getIsAdmin(), user.getId(), user.getUserStatus().getId());
    }


    /**
     * methodName : verifyUserStatusByEmail
     * author : masiljangajji
     * description : 로그인 처리를 진행함
     * 이메일을 이용해 유저가 존재하는지 확인 후 암호화된 비밀번호를 응답
     *
     * @param request 이메일
     * @return user encrypted password response 암호화된 비밀번호
     * @throws UserAlreadyResignException 탈퇴한 회원인 경우
     * @throws UserLoginFailException     이메일이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public UserEncryptedPasswordResponse verifyUserStatusByEmail(UserEmailRequest request) {

        // 아이디 확인
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserLoginFailException::new);

        // 탈퇴한 회원인지 확인
        if (user.getUserStatus().getId().equals(UserStatusEnum.RESIGN.toString())) {
            throw new UserAlreadyResignException();
        }

        return new UserEncryptedPasswordResponse(user.getPassword());
    }

    /**
     * methodName : verifyDormancyUser
     * author : masiljangajji
     * description : 휴면회원을 인증함
     *
     * @param id id
     * @return user inactive verification response
     */
    public UserInactiveVerificationResponse verifyDormancyUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException(id));
        UserStatus userStatus = userStatusRepository.findById(UserStatusEnum.ACTIVE.toString())
                .orElseThrow(() -> new UserStatusNotExistException(UserStatusEnum.ACTIVE.toString()));
        user.modifyUserStatus(userStatus);
        return new UserInactiveVerificationResponse(userStatus.getId());
    }

    /**
     * methodName : verifyLockUser
     * author : masiljangajji
     * description :잠금회원을 인증함
     * 잠금회원인 경우 비밀번호를 변경해야 함
     *
     * @param id      id
     * @param request 비밀번호
     * @return user inactive verification response
     */
    public UserInactiveVerificationResponse verifyLockUser(Long id, UserPasswordModifyRequest request) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException(id));
        UserStatus userStatus = userStatusRepository.findById(UserStatusEnum.ACTIVE.toString())
                .orElseThrow(() -> new UserStatusNotExistException(UserStatusEnum.ACTIVE.toString()));
        user.modifyUserStatus(userStatus);
        user.modifyPassword(request.getPassword());
        return new UserInactiveVerificationResponse(userStatus.getId());
    }

    /**
     * methodName : verifyUserEmail
     * author : masiljangajji
     * description : 회원가입시 사용 가능한 이메일인지 검증
     *
     * @param request 이메일
     * @return user email check response
     */
    public UserEmailCheckResponse verifyUserEmail(UserEmailRequest request) {
        return new UserEmailCheckResponse(!userRepository.existsByEmail(request.getEmail()));
    }

}

