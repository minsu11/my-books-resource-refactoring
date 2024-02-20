package store.mybooks.resource.user_grade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import store.mybooks.resource.user_grade.exception.UserGradeAlreadyUsedException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_grade_name.repository.UserGradeNameRepository;

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

    private final UserGradeNameRepository userGradeNameRepository;


    /**
     * Create user grade user grade create response.
     * <p>
     * UserGrade 생성
     * UserGradeName이 존재하지않는다면 UserGradeNameNotExistException
     *
     * @param createRequest the create request
     * @return the user grade create response
     */
    @Transactional
    public UserGradeCreateResponse createUserGrade(UserGradeCreateRequest createRequest) {

        String userGradeNameRequest = createRequest.getName();

        UserGradeName userGradeName = userGradeNameRepository.findById(userGradeNameRequest)
                // todo 이미 사용중인 UserGrade가 있다면 사용중인 등급의 isAvailable 을 false 로 변경하고 새로운 등급을 넣는거 고민해보기
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeNameRequest));

        UserGrade userGrade =
                new UserGrade(createRequest.getMinCost(), createRequest.getMaxCost(), createRequest.getRate(),
                        createRequest.getCreatedDate(), userGradeName);


        // 이미사용중인거 있으면 에러
        if (userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeNameRequest).isPresent()) {
            throw new UserGradeAlreadyUsedException(userGradeNameRequest);
        }

        userGradeRepository.save(userGrade);

        return UserGradeMapper.INSTANCE.toUserGradeCreateResponse(userGrade);
    }

    /**
     * Delete user grade user grade delete response.
     * <p>
     * UserGrade를 id를 이용해 삭제함
     * 강 삭제가 아닌 isAvailable Field를 flase로 변경하는 약삭제를 제공
     * 삭제하려는 UserGradeId가 존재하지 않는다면 UserGradeIdNotExistException
     *
     * @param id the id
     * @return the user grade delete response
     */
    @Transactional
    public UserGradeDeleteResponse deleteUserGrade(Integer id) {

        UserGrade userGrade = userGradeRepository.findById(id).orElseThrow(UserGradeIdNotExistException::new);

        userGrade.deleteUserGrade();
        return new UserGradeDeleteResponse("UserGrade 삭제완료");
    }

    /**
     * Find user grade by id user grade get response.
     * <p>
     * UserGrade를 id를 이용해 찾음
     * 찾으려는 UserGradeId가 존재하지 않는다면 UserGradeIdNotExistException
     *
     * @param id the id
     * @return the user grade get response
     */
    public UserGradeGetResponse findUserGradeById(Integer id) {

        userGradeRepository.findById(id).orElseThrow(UserGradeIdNotExistException::new);

        return userGradeRepository.queryById(id);
    }


    /**
     * Find all user grade page.
     * <p>
     * UserGrade를 Pagination 해서 보여줌
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    public Page<UserGradeGetResponse> findAllUserGrade(Pageable pageable) {
        return userGradeRepository.queryAllBy(pageable);
    }


}
