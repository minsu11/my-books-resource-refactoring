package store.mybooks.resource.user_grade.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user_grade.dto.mapper.UserGradeMapper;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.exception.UserGradeIdNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_grade_name.repository.UserGradeNameRepository;

/**
 * packageName    : store.mybooks.resource.user_grade.service<br>
 * fileName       : UserGradeService<br>
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
public class UserGradeService {

    private final UserGradeRepository userGradeRepository;

    private final UserGradeNameRepository userGradeNameRepository;

    private final UserGradeMapper userGradeMapper;


    /**
     * methodName : createUserGrade
     * author : masiljangajji
     * description : 유저 등급을 생성
     * 유저등급은 추가될시 자동으로 기존의 등급을 대체함 (기존 등급은 비활성으로 변경)
     * @param createRequest request
     * @return user grade create response
     * @throws UserGradeNameNotExistException 유저등급 이름이 존재하지 않는 경우
     */
    @Transactional
    public UserGradeCreateResponse createUserGrade(UserGradeCreateRequest createRequest) {

        String userGradeNameRequest = createRequest.getUserGradeNameId();

        UserGradeName userGradeName = userGradeNameRepository.findById(userGradeNameRequest)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeNameRequest));

        Optional<UserGrade> optionalUserGrade =
                userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeNameRequest);


        if (optionalUserGrade.isEmpty()) {
            throw new UserGradeIdNotExistException();
        }

        UserGrade userGrade = optionalUserGrade.get();
        userGrade.deleteUserGrade();

        UserGrade resultUserGrade =
                new UserGrade(userGrade.getMinCost(), userGrade.getMaxCost(), createRequest.getRate(), userGradeName);

        userGradeRepository.save(resultUserGrade);
        return userGradeMapper.toUserGradeCreateResponse(userGrade);
    }


    /**
     * methodName : findAllUserGrade
     * author : masiljangajji
     * description : 모든 유저 등급을 확인 (비활성 상태 + 활성상태)
     *
     * @return list
     */

    public List<UserGradeGetResponse> findAllAvailableUserGrade() {
        return userGradeRepository.queryAllByIsAvailableIsTrueOrderByMinCost();
    }

    public List<UserGradeGetResponse> findAllUserGrade() {
        return userGradeRepository.queryAllByOrderByMinCost();
    }
}
