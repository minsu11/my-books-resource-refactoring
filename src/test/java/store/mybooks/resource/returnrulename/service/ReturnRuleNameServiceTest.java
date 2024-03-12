package store.mybooks.resource.returnrulename.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.mybooks.resource.returnrulename.dto.mapper.ReturnRuleNameMapper;
import store.mybooks.resource.returnrulename.dto.request.ReturnRuleNameCreateRequest;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameCreateResponse;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;
import store.mybooks.resource.returnrulename.exception.ReturnRuleNameAlreadyExistException;
import store.mybooks.resource.returnrulename.exception.ReturnRuleNameNotExistException;
import store.mybooks.resource.returnrulename.repository.ReturnRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.returnrulename.service<br>
 * fileName       : ReturnRuleNameServiceTest<br>
 * author         : minsu11<br>
 * date           : 3/10/24<br>
 * description    : 반품 규정 명 서비스 테스트.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/10/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
class ReturnRuleNameServiceTest {
    @InjectMocks
    private ReturnRuleNameService returnRuleNameService;

    @Mock
    ReturnRuleNameRepository returnRuleNameRepository;

    @Mock
    ReturnRuleNameMapper returnRuleNameMapper;

    @Test
    @DisplayName("id로 반품 규정 명 조회")
    void givenId_whenFindReturnRuleNameByName_thenReturnReturnRuleNameResponse() {
        ReturnRuleNameResponse expected = new ReturnRuleNameResponse("test", LocalDate.of(1212, 12, 12));
        Mockito.when(returnRuleNameRepository.findReturnRuleNameById(anyString())).thenReturn(Optional.of(expected));
        ReturnRuleNameResponse actual = returnRuleNameService.getReturnRuleName("test");
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        verify(returnRuleNameRepository, times(1)).findReturnRuleNameById(any());
    }

    @Test
    @DisplayName("id에 맞는 반품 규정 명이 없는 경우")
    void givenId_whenFindReturnRuleNameById_thenThrowReturnRuleNameNotExistException() {
        Mockito.when(returnRuleNameRepository.findReturnRuleNameById(anyString())).thenThrow(ReturnRuleNameNotExistException.class);
        Assertions.assertThrows(ReturnRuleNameNotExistException.class, () -> returnRuleNameService.getReturnRuleName("test"));
        verify(returnRuleNameRepository, times(1)).findReturnRuleNameById(anyString());
    }

    @Test
    @DisplayName("전체 반품 규정 명 조회")
    void whenGetReturnRuleNameList_thenReturnReturnRuleNameList() {
        List<ReturnRuleNameResponse> expected =
                List.of(new ReturnRuleNameResponse("test", LocalDate.of(1212, 12, 12)));
        Mockito.when(returnRuleNameRepository.getReturnRuleNameList()).thenReturn(expected);
        List<ReturnRuleNameResponse> actual = returnRuleNameService.getReturnRuleNameList();

        Assertions.assertEquals(expected, actual);
        verify(returnRuleNameRepository, times(1)).getReturnRuleNameList();
    }

    @Test
    @DisplayName("DB에 저장된 값이 없을 경우 빈 리스트 반환")
    void whenGetReturnRuleNameList_thenReturnEmptyList() {
        when(returnRuleNameRepository.getReturnRuleNameList()).thenReturn(Collections.emptyList());
        List<ReturnRuleNameResponse> actual = returnRuleNameService.getReturnRuleNameList();
        Assertions.assertEquals(0, actual.size());

    }

    @Test
    @DisplayName("반품 규정 명 등록")
    void givenReturnRuleNameCreateRequest_whenSave_thenReturnReturnRuleNameCreateResponse(@Mock ReturnRuleName returnRuleName) {
        ReturnRuleNameCreateResponse expected = new ReturnRuleNameCreateResponse("test", LocalDate.of(1212, 12, 12));
        ReturnRuleNameCreateRequest request = new ReturnRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", "test12");
        when(returnRuleNameRepository.existsById(anyString())).thenReturn(false);
        when(returnRuleNameRepository.save(any(ReturnRuleName.class))).thenReturn(returnRuleName);
        when(returnRuleNameMapper.mapToReturnRuleNameCreateResponse(any())).thenReturn(expected);

        ReturnRuleNameCreateResponse actual = returnRuleNameService.createReturnRuleName(request);

        Assertions.assertEquals(expected.getId(), actual.getId());


        verify(returnRuleNameRepository, times(1)).existsById(anyString());
        verify(returnRuleNameRepository, times(1)).save(any());
        verify(returnRuleNameMapper, times(1)).mapToReturnRuleNameCreateResponse(any());

    }

    @Test
    @DisplayName("반품 규정 명 등록 실패")
    void givenReturnRuleNameCreateRequest_whenExistById_thenThrowReturnRuleNameAlreadyExistException() {
        ReturnRuleNameCreateRequest request = new ReturnRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", "test12");

        when(returnRuleNameRepository.existsById(anyString())).thenReturn(true);

        Assertions.assertThrows(ReturnRuleNameAlreadyExistException.class, () -> returnRuleNameService.createReturnRuleName(request));

        verify(returnRuleNameRepository, times(1)).existsById(anyString());
        verify(returnRuleNameRepository, never()).save(any());
        verify(returnRuleNameMapper, never()).mapToReturnRuleNameCreateResponse(any());

    }
}