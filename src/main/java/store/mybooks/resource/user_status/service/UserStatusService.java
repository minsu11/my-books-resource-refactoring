package store.mybooks.resource.user_status.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user_status.service<br>
 * fileName       : UserStatusService<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;


    /**
     * methodName : findUserStatusById
     * author : masiljangajji
     * description : 유저상태의 정보를 찾음
     *
     * @param id id
     * @return user status get response
     * @throws UserStatusNotExistException 유저상태가 존재하지 않는 경우
     */
    public UserStatusGetResponse findUserStatusById(String id) {

        if (!userStatusRepository.existsById(id)) {
            throw new UserStatusNotExistException(id);
        }
        return userStatusRepository.queryById(id);
    }

    /**
     * methodName : findAllUserStatus
     * author : masiljangajji
     * description : 모든 유저상태 정보를 찾음
     *
     * @return list
     */
    public List<UserStatusGetResponse> findAllUserStatus() {
        return userStatusRepository.queryAllBy();
    }


}
