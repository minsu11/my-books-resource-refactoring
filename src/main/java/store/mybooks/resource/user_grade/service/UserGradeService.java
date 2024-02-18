package store.mybooks.resource.user_grade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user_grade.dto.mapper.UserGradeCreateMapper;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeDeleteResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.exception.UserGradeNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;

/**
 * packageName    : store.mybooks.resource.user_grade.service
 * fileName       : UserGradeService
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
@Transactional(readOnly = true)
public class UserGradeService {

    private final UserGradeRepository userGradeRepository;

    @Transactional
    public UserGradeCreateResponse createUserGrade(UserGradeCreateRequest createRequest) {

        UserGrade userGrade = new UserGrade(createRequest);

        userGradeRepository.save(userGrade);

        return UserGradeCreateMapper.INSTANCE.toUserGradeCreateResponse(userGrade);
    }

    @Transactional
    public UserGradeDeleteResponse deleteUserGrade(Integer id) {

        userGradeRepository.findById(id).orElseThrow(() -> new UserGradeNotExistException(id));
        userGradeRepository.deleteById(id);
        return new UserGradeDeleteResponse(String.format("[%d]번 UserGrade 삭제완료", id));
    }

    public UserGradeGetResponse findUserGradeById(Integer id) {

        userGradeRepository.findById(id).orElseThrow(() -> new UserGradeNotExistException(1));

        return userGradeRepository.queryById(id);
    }


}
