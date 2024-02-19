package store.mybooks.resource.user_status.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.user_status.dto.mapper.UserStatusCreateMapper;
import store.mybooks.resource.user_status.dto.request.UserStatusCreateRequest;
import store.mybooks.resource.user_status.dto.response.UserStatusCreateResponse;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.exception.UserStatusAlreadyExistException;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user_status.service
 * fileName       : UserStatusService
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;




    public UserStatusGetResponse findUserStatusById(String id) {

        userStatusRepository.findById(id).orElseThrow(() -> new UserStatusNotExistException(id));
        return userStatusRepository.queryById(id);
    }


}
