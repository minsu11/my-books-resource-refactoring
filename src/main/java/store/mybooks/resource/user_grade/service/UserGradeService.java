package store.mybooks.resource.user_grade.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user_grade.dto.mapper.UserGradeMapper;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeDeleteResponse;
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
     *
     * @param createRequest request
     * @throws UserGradeNameNotExistException 유저의 이름이 존재하지 않는 경우
     * @return user grade create response
     */
    @Transactional
    public UserGradeCreateResponse createUserGrade(UserGradeCreateRequest createRequest) {

        String userGradeNameRequest = createRequest.getName();

        UserGradeName userGradeName = userGradeNameRepository.findById(userGradeNameRequest)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeNameRequest));

        UserGrade userGrade =
                new UserGrade(createRequest.getMinCost(), createRequest.getMaxCost(), createRequest.getRate(),
                        createRequest.getCreatedDate(), userGradeName);


        // todo 이미 사용중인 UserGrade가 있다면 사용중인 등급의 isAvailable 을 false 로 변경하고 새로운 등급을 넣는걸로 변경

        Optional<UserGrade> optionalUserGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeNameRequest);
        optionalUserGrade.ifPresent(UserGrade::deleteUserGrade);

        userGradeRepository.save(userGrade);

        return userGradeMapper.toUserGradeCreateResponse(userGrade);
    }

    /**
     * methodName : deleteUserGrade
     * author : masiljangajji
     * description : 유저 등급을 삭제
     *
     * @param id id
     * @throws UserGradeIdNotExistException 유저등급이 존재하지 않는 경우
     * @return user grade delete response
     */
    @Transactional
    public UserGradeDeleteResponse deleteUserGrade(Integer id) {

        UserGrade userGrade = userGradeRepository.findById(id).orElseThrow(UserGradeIdNotExistException::new);

        userGrade.deleteUserGrade();
        return new UserGradeDeleteResponse("UserGrade 삭제완료");
    }

    /**
     * methodName : findUserGradeById
     * author : masiljangajji
     * description : 유저등급을 찾음
     *
     * @param id id
     * @throws UserGradeIdNotExistException 유저등급이 존재하지 않는 경우
     * @return user grade get response
     */
    public UserGradeGetResponse findUserGradeById(Integer id) {

        userGradeRepository.findById(id).orElseThrow(UserGradeIdNotExistException::new);

        return userGradeRepository.queryById(id);
    }


    /**
     * methodName : findAllUserGrade
     * author : masiljangajji
     * description : 사용중인 모든 유저등급 정보를 찾음
     *
     * @return list
     */
    public List<UserGradeGetResponse> findAllUserGrade() {
        return userGradeRepository.queryAllByAndIsAvailableIsTrue();
    }


}
