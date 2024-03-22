package store.mybooks.resource.user_grade.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.user_grade.dto.mapper.UserGradeMapper;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_grade_name.repository.UserGradeNameRepository;

/**
 * packageName    : store.mybooks.resource.user_grade.service
 * fileName       : UserGradeServiceTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */

@ExtendWith(MockitoExtension.class)
class UserGradeServiceTest {

    @InjectMocks
    UserGradeService userGradeService;

    @Mock
    UserGradeRepository userGradeRepository;

    @Mock
    UserGradeNameRepository userGradeNameRepository;

    @Mock
    UserGradeMapper userGradeMapper;


    @Test
    @DisplayName("UserGradeCreateRequest 로 createUserGrade 메서드 실행시 동작 테스트")
    void givenUserGradeRequest_whenCallCreateUserGrade_thenReturnUserGradeCreateResponse(
            @Mock UserGradeCreateRequest userGradeCreateRequest,
            @Mock UserGradeName userGradeName,
            @Mock UserGrade newUserGrade,
            @Mock UserGradeCreateResponse userGradeCreateResponse,
            @Mock UserGrade userGrade) {


        when(userGradeMapper.toUserGradeCreateResponse(any(UserGrade.class))).thenReturn(userGradeCreateResponse);
        when(userGradeNameRepository.findById(anyString())).thenReturn(Optional.of(userGradeName));
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(
                Optional.of(userGrade));

        when(userGradeRepository.save(any(UserGrade.class))).thenReturn(newUserGrade);
        when(userGradeCreateRequest.getUserGradeNameId()).thenReturn("test");

        userGradeService.createUserGrade(userGradeCreateRequest);

        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(any());
        verify(userGradeNameRepository, times(1)).findById(any());
        verify(userGradeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("UserGradeCreateRequest 로 createUserGrade 메서드 실행시 UserGradeName 이 없는 경우 UserGradeNameNotExistException")
    void givenNotExistUserGradeName_whenCallCreateUserGrade_thenThrowUserGradeNameNotExistException(
            @Mock UserGradeCreateRequest userGradeCreateRequest) {

        when(userGradeCreateRequest.getUserGradeNameId()).thenReturn("test");

        assertThrows(UserGradeNameNotExistException.class,
                () -> userGradeService.createUserGrade(userGradeCreateRequest));
    }

    @Test
    @DisplayName("findAllUserGrade 메서드 실행시 동작 테스트")
    void givenPageable_whenCallFindAllUserGrade_thenReturnUserGradeGetResponsePage(@Mock List<UserGradeGetResponse> list) {

        when(userGradeRepository.queryAllByOrderByMinCost()).thenReturn(list);

        userGradeService.findAllUserGrade();

        verify(userGradeRepository, times(1)).queryAllByOrderByMinCost();
    }
}