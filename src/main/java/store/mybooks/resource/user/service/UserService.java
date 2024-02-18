package store.mybooks.resource.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import store.mybooks.resource.user.dto.mapper.UserCreateMapper;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.mapper.UserModifyMapper;
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
        String userGradeName = UserGradeEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        // todo 이거 변경가능함
        UserGrade userGrade = userGradeRepository.findByName(userGradeName)
                .orElseThrow(() -> new UserGradeNotExistException(1));


        User user = new User(createRequest, userStatus, userGrade);

        userRepository.save(user);

        // todo 이거 mapstruct 써서 변경할 수 있음
        return UserCreateMapper.INSTANCE.toUserCreateResponse(user);
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
        UserGrade userGrade = userGradeRepository.findByName(userGradeName).orElseThrow(() -> new RuntimeException());

        user.setByModifyRequest(modifyRequest, userStatus, userGrade);

        return UserModifyMapper.INSTANCE.toUserModifyResponse(user);

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

    public Page<UserGetResponse> findAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.queryAllBy(pageable);
    }


}

