package store.mybooks.resource.pointrulename.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.mybooks.resource.pointrulename.dto.mapper.PointRuleNameMapper;
import store.mybooks.resource.pointrulename.dto.request.PointRuleNameCreateRequest;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameCreateResponse;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.exception.PointRuleNameAlreadyExistException;
import store.mybooks.resource.pointrulename.exception.PointRuleNameNotExistException;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.point_rule_name.service<br>
 * fileName       : PointRuleNameServiceUnitTest<br>
 * author         : minsu11<br>
 * date           : 3/8/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/8/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
class PointRuleNameServiceUnitTest {
    @InjectMocks
    PointRuleNameService pointRuleNameService;

    @Mock
    PointRuleNameRepository pointRuleNameRepository;

    @Mock
    PointRuleNameMapper pointRuleNameMapper;

    @Test
    @DisplayName("id에 따른 조회 성공 테스트 ")
    void givenId_whenGetPointRuleNameById_thenReturnPointRuleNameResponse() {
        String id = "test";
        PointRuleNameResponse expected = new PointRuleNameResponse("test");
        when(pointRuleNameRepository.getPointRuleNameById(any())).thenReturn(Optional.of(expected));

        PointRuleNameResponse actual = pointRuleNameService.getPointRuleName(id);

        Assertions.assertEquals(expected.getId(), actual.getId());

        verify(pointRuleNameRepository, times(1)).getPointRuleNameById(any());


    }

    @Test
    @DisplayName("id에 따른 조회 실패 테스트")
    void givenId_whenGetPointRuleNameById_thenThrowPointRuleNameNotExistException() {
        String id = "test2";
        when(pointRuleNameRepository.getPointRuleNameById(any())).thenThrow(PointRuleNameNotExistException.class);
        Assertions.assertThrows(PointRuleNameNotExistException.class, () -> pointRuleNameService.getPointRuleName(id));
        verify(pointRuleNameRepository, times(1)).getPointRuleNameById(any());
    }


    @Test
    @DisplayName("포인트 규정 명 목록 조회")
    void given_whenGetPointRuleNameList_thenReturnPointRuleNameResponseList() {
        List<PointRuleNameResponse> expected = List.of(new PointRuleNameResponse("test"));
        when(pointRuleNameRepository.getPointRuleNameList()).thenReturn(expected);
        List<PointRuleNameResponse> actual = pointRuleNameService.getPointRuleNameList();

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        verify(pointRuleNameRepository, times(1)).getPointRuleNameList();
    }

    @Test
    @DisplayName("DB에 포인트 규정이 없을 경우")
    void given_whenGetPointRuleNameList_thenReturnEmptyList() {
        when(pointRuleNameRepository.getPointRuleNameList()).thenReturn(Collections.emptyList());
        List<PointRuleNameResponse> actual = pointRuleNameService.getPointRuleNameList();
        Assertions.assertEquals(0, actual.size());

    }


    @Test
    @DisplayName("포인트 규정 명 생성")
    void givenPointRuleNameCreateRequest_whenSave_thenReturnPointRuleNameCreateResponse() {
        String testId = "test";
        PointRuleNameCreateRequest request = new PointRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", testId);
        PointRuleName pointRuleName = new PointRuleName(testId);
        PointRuleNameCreateResponse expected = new PointRuleNameCreateResponse(testId);
        when(pointRuleNameRepository.existsById(any())).thenReturn(false);
        when(pointRuleNameRepository.save(any())).thenReturn(pointRuleName);
        when(pointRuleNameMapper.mapToPointRuleNameCreateResponse(any())).thenReturn(expected);

        PointRuleNameCreateResponse actual = pointRuleNameService.createPointRuleName(request);

        Assertions.assertEquals(expected.getId(), actual.getId());

        verify(pointRuleNameRepository, times(1)).existsById(any());
        verify(pointRuleNameRepository, times(1)).save(any());
        verify(pointRuleNameMapper, times(1)).mapToPointRuleNameCreateResponse(any());
    }

    @Test
    @DisplayName("포인트 규정 명 중복 값 등록 시 예외 처리")
    void givenPointRuleNameCreateRequest_whenExistsById_thenThrowPointRuleNameAlreadyExistException() {
        PointRuleNameCreateRequest request = new PointRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", "test");
        when(pointRuleNameRepository.existsById(any())).thenReturn(true);

        Assertions.assertThrows(PointRuleNameAlreadyExistException.class, () -> pointRuleNameService.createPointRuleName(request));
        verify(pointRuleNameRepository, times(1)).existsById(any());
        verify(pointRuleNameRepository, never()).save(any());
        verify(pointRuleNameMapper, never()).mapToPointRuleNameCreateResponse(any());
    }


}