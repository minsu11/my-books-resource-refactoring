package store.mybooks.resource.user_grade.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            @Mock UserGrade userGrade,
            @Mock UserGradeCreateResponse userGradeCreateResponse) {


        when(userGradeMapper.toUserGradeCreateResponse(any(UserGrade.class))).thenReturn(userGradeCreateResponse);
        when(userGradeNameRepository.findById(anyString())).thenReturn(Optional.of(userGradeName));
        when(userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(anyString())).thenReturn(Optional.empty());

        when(userGradeRepository.save(any(UserGrade.class))).thenReturn(userGrade);
        when(userGradeCreateRequest.getName()).thenReturn("test");

        userGradeService.createUserGrade(userGradeCreateRequest);

        verify(userGradeRepository, times(1)).findByUserGradeNameIdAndIsAvailableIsTrue(any());
        verify(userGradeNameRepository, times(1)).findById(any());
        verify(userGradeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("UserGradeCreateRequest 로 createUserGrade 메서드 실행시 UserGradeName 이 없는 경우 UserGradeNameNotExistException")
    void givenNotExistUserGradeName_whenCallCreateUserGrade_thenThrowUserGradeNameNotExistException(
            @Mock UserGradeCreateRequest userGradeCreateRequest) {

        when(userGradeCreateRequest.getName()).thenReturn("test");

        assertThrows(UserGradeNameNotExistException.class,
                () -> userGradeService.createUserGrade(userGradeCreateRequest));
    }


    @Test
    @DisplayName("userGradeId 로 DeleteUserGrade 메서드 실행시 동작 테스트")
    void givenUserGradeId_whenCallDeleteUserGrade_thenReturnUserGradeDeleteResponse(@Mock UserGrade userGrade) {

        when(userGradeRepository.findById(any())).thenReturn(Optional.of(userGrade));

        userGradeService.deleteUserGrade(1);
        verify(userGradeRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("존재하지 않는 userGradeId 로 DeleteUserGrade 메서드 실행시 UserGradeIdNotExistException")
    void givenNotExistUserGradeId_whenCallDeleteUserGrade_thenThrowUserGradeNotExistException(
            @Mock UserGrade userGrade) {

        when(userGradeRepository.findById(1)).thenReturn(Optional.of(userGrade));

        userGradeService.deleteUserGrade(1);
        verify(userGradeRepository, times(1)).findById(any());

        assertThrows(UserGradeIdNotExistException.class, () -> userGradeService.deleteUserGrade(10000));
    }

    @Test
    @DisplayName("UserGradeId 로 findUserGradeById 메서드 실행시 동작 테스트")
    void givenUserGradeId_whenCallFindUserGradeById_thenReturnUserGradeGetResponse(
            @Mock UserGrade userGrade) {

        when(userGradeRepository.findById(any())).thenReturn(Optional.of(userGrade));
        userGradeService.findUserGradeById(any());
        verify(userGradeRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("findAllUserGrade 메서드 실행시 동작 테스트")
    void givenPageable_whenCallFindAllUserGrade_thenReturnUserGradeGetResponsePage(@Mock List<UserGradeGetResponse> list) {

        when(userGradeRepository.queryAllByAndIsAvailableIsTrue()).thenReturn(list);

        userGradeService.findAllUserGrade();

        verify(userGradeRepository, times(1)).queryAllByAndIsAvailableIsTrue();
    }
}