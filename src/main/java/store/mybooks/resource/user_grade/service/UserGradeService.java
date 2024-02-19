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

    @Transactional
    public UserGradeCreateResponse createUserGrade(UserGradeCreateRequest createRequest) {

        String userGradeNameRequest = createRequest.getName();

        UserGradeName userGradeName = userGradeNameRepository.findById(userGradeNameRequest)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeNameRequest));

        UserGrade userGrade = new UserGrade(createRequest, userGradeName);


        // 이미사용중인거 있으면 에러
        if (userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeNameRequest).isPresent()) {
            throw new UserGradeAlreadyUsedException(userGradeNameRequest);
        }

        userGradeRepository.save(userGrade);

        return UserGradeMapper.INSTANCE.toUserGradeCreateResponse(userGrade);
    }

    @Transactional
    public UserGradeDeleteResponse deleteUserGrade(Integer id) {

        UserGrade userGrade = userGradeRepository.findById(id).orElseThrow(UserGradeIdNotExistException::new);

        userGrade.modifyIsAvailable(false);
        return new UserGradeDeleteResponse("UserGrade 삭제완료");
    }

    public UserGradeGetResponse findUserGradeById(Integer id) {

        userGradeRepository.findById(id).orElseThrow(UserGradeIdNotExistException::new);

        return userGradeRepository.queryById(id);
    }


    public Page<UserGradeGetResponse> findAllUserGrade(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return userGradeRepository.queryAllBy(pageable);
    }


}
