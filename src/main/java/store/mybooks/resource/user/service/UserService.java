package store.mybooks.resource.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user.dto.mapper.UserMapper;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserAlreadyExistException;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.exception.UserGradeAlreadyUsedException;
import store.mybooks.resource.user_grade.exception.UserGradeIdNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.enumeration.UserStatusEnum;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user.service
 * fileName       : UserService
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */


@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {


    private final UserRepository userRepository;

    private final UserStatusRepository userStatusRepository;

    private final UserGradeRepository userGradeRepository;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest createRequest) {

        // 이미 존재하면 예외처리
        if (userRepository.findByEmail(createRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException(createRequest.getEmail());
        }

        String userStatusName = UserStatusEnum.ACTIVE.toString();
        String userGradeName = UserGradeNameEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeName));


        User user = new User(createRequest, userStatus, userGrade);

        userRepository.save(user);

        return UserMapper.INSTANCE.toUserCreateResponse(user);
    }

    @Transactional
    public UserModifyResponse modifyUser(String email, UserModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotExistException(email));

        String userStatusName = modifyRequest.getUserStatusName();
        String userGradeName = modifyRequest.getUserGradeName();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));
        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName).orElseThrow(
                UserGradeIdNotExistException::new);

        user.setByModifyRequest(modifyRequest, userStatus, userGrade);

        return UserMapper.INSTANCE.toUserModifyResponse(user);

    }

    @Transactional
    public UserDeleteResponse deleteUser(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotExistException(email));

        String userStatusName = UserStatusEnum.INACTIVE.toString();
        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        user.modifyUserStatus(userStatus);
        return new UserDeleteResponse(String.format("[%s] 유저 삭제완료", email));
    }

    public UserGetResponse findByEmail(String email) {

        userRepository.findByEmail(email).orElseThrow(() -> new UserNotExistException(email));

        return userRepository.queryByEmail(email);
    }

    public Page<UserGetResponse> findAllUser(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.queryAllBy(pageable);
    }


}

