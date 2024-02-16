package store.mybooks.resource.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import store.mybooks.resource.user_grade.enumeration.UserGradeEnum;
import store.mybooks.resource.user_grade.exception.UserGradeNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
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
        String userGradeName = UserGradeEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));
        UserGrade userGrade = userGradeRepository.findByName(userGradeName)
                .orElseThrow(() -> new UserGradeNotExistException(userGradeName));

        User user = new User(createRequest, userStatus, userGrade);

        User resultUser = userRepository.save(user);

        return resultUser.convertToCreateResponse();
    }

    @Transactional
    public UserModifyResponse modifyUser(UserModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findByEmail(modifyRequest.getEmail())
                .orElseThrow(() -> new UserNotExistException(modifyRequest.getEmail()));

        UserStatus userStatus = userStatusRepository.findById(modifyRequest.getUserStatusName()).orElseThrow();
        UserGrade userGrade = userGradeRepository.findByName(modifyRequest.getUserGradeName()).orElseThrow();


        user.setByModifyRequest(modifyRequest, userStatus, userGrade);


        return user.convertToModifyResponse();

    }

    @Transactional
    public UserDeleteResponse deleteUser(String email) {

        userRepository.findByEmail(email).orElseThrow(() -> new UserNotExistException(email));

        // 이거 소프트삭제로 변경 필요함
        userRepository.deleteByEmail(email);

        return new UserDeleteResponse("유저 Soft 삭제완료");
    }

    @Transactional(readOnly = true)
    public UserGetResponse findByEmail(String email) {

        userRepository.findByEmail(email).orElseThrow(() -> new UserNotExistException(email));

        return userRepository.queryByEmail(email);
    }


}

