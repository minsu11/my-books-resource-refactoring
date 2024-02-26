package store.mybooks.resource.return_rule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.return_rule.dto.mapper.ReturnRuleMapper;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleCreateRequest;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleModifyRequest;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleCreateResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleDeleteResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleModifyResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.entity.ReturnRule;
import store.mybooks.resource.return_rule.exception.ReturnRuleAlreadyExistException;
import store.mybooks.resource.return_rule.exception.ReturnRuleNotExistException;
import store.mybooks.resource.return_rule.repository.ReturnRuleRepository;
import store.mybooks.resource.return_rule_name.entity.ReturnRuleName;
import store.mybooks.resource.return_rule_name.exception.ReturnRuleNameNotExistException;
import store.mybooks.resource.return_rule_name.repository.ReturnRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.return_rule.service<br>
 * fileName       : ReturnRuleServiceTest<br>
 * author         : minsu11<br>
 * date           : 2/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/26/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
class ReturnRuleServiceTest {

    @InjectMocks
    ReturnRuleService returnRuleService;

    @Mock
    ReturnRuleNameRepository returnRuleNameRepository;

    @Mock
    ReturnRuleRepository returnRuleRepository;

    @Mock
    ReturnRuleMapper returnRuleMapper;

    @Test
    @DisplayName("id의 값으로 반품 규정 조회 성공 테스트")
    void givenReturnRuleName_whenFindByReturnRuleName_thenReturnReturnRuleResponse() {
        String test = "test";
        ReturnRuleResponse expected = new ReturnRuleResponse("test", 1000, 10, true);
        when(returnRuleRepository.findByReturnRuleName(test)).thenReturn(Optional.of(expected));
        ReturnRuleResponse actual = returnRuleService.getReturnRuleResponseByReturnRuleName(test);
        assertEquals(expected, actual);
        verify(returnRuleRepository, times(1)).findByReturnRuleName(anyString());
    }

    @Test
    @DisplayName("id의 값으로 반품 규정 조회 실패 테스트")
    void givenReturnRuleName_whenFindByReturnRuleName_thenThrowReturnRuleNotExistException() {
        String test = "test";
        when(returnRuleRepository.findByReturnRuleName(test)).thenThrow(ReturnRuleNotExistException.class);

        assertThrows(ReturnRuleNotExistException.class, () -> returnRuleService.getReturnRuleResponseByReturnRuleName(test));
        verify(returnRuleRepository, times(1)).findByReturnRuleName(anyString());
    }

    @Test
    @DisplayName("전체 반품 규정 조회 성공 테스트")
    void given_whenGetReturnRuleResponseList_thenReturnReturnRuleResponseList() {

        List<ReturnRuleResponse> expected = List.of(new ReturnRuleResponse("test", 1000, 10, true));
        when(returnRuleRepository.getReturnRuleResponseList()).thenReturn(expected);
        List<ReturnRuleResponse> actual = returnRuleService.getReturnRuleResponseList();

        assertEquals(expected, actual);
        verify(returnRuleRepository, times(1)).getReturnRuleResponseList();
    }

    @Test
    @DisplayName("전체 반품 규정 조회 시 DB 저장된 값이 없어서 빈 List가 반환되는 테스트")
    void given_whenGetReturnRuleResponseList_thenReturnEmptyList() {
        List<ReturnRuleResponse> expected = Collections.emptyList();
        when(returnRuleRepository.getReturnRuleResponseList()).thenReturn(expected);
        List<ReturnRuleResponse> actual = returnRuleService.getReturnRuleResponseList();

        assertEquals(expected, actual);
        verify(returnRuleRepository, times(1)).getReturnRuleResponseList();
    }


    @Test
    @DisplayName("반품 정책 등록 성공 테스트")
    void givenReturnRuleCreateRequest_whenSave_thenReturnReturnRuleCreateResponse() {
        String testId = "test";
        ReturnRuleName expectedReturnRuleName = new ReturnRuleName(testId, LocalDate.of(1212, 12, 12));
        ReturnRule expectedReturnRule = new ReturnRule(1L, 1000, 10, true,
                LocalDate.of(1212, 12, 12), expectedReturnRuleName);
        ReturnRuleCreateResponse expected = new ReturnRuleCreateResponse(expectedReturnRuleName.getId(),
                expectedReturnRule.getDeliveryFee(), expectedReturnRule.getTerm(), expectedReturnRule.getIsAvailable());

        when(returnRuleRepository.findByReturnRuleName(anyString())).thenReturn(Optional.empty());
        when(returnRuleNameRepository.findById(anyString())).thenReturn(Optional.of(expectedReturnRuleName));
        when(returnRuleRepository.save(any(ReturnRule.class))).thenReturn(expectedReturnRule);
        when(returnRuleMapper.mapToReturnRuleCreateResponse(any(ReturnRule.class))).thenReturn(expected);

        ReturnRuleCreateRequest request = new ReturnRuleCreateRequest(testId, 1000, 10);
        ReturnRuleCreateResponse actual = returnRuleService.createReturnRule(request);
        assertEquals(expected, actual);

        verify(returnRuleNameRepository, times(1)).findById(anyString());
        verify(returnRuleRepository, times(1)).save(any());
        verify(returnRuleMapper, times(1)).mapToReturnRuleCreateResponse(any());

    }

    @Test
    @DisplayName("반품 규정 등록 실패 테스트(반품 규정이 존재)")
    void givenReturnCreateRequest_whenFindByReturnRuleName_thenThrowReturnRuleNameAlreadyExistException() {
        when(returnRuleRepository.findByReturnRuleName(anyString())).thenReturn(Optional.of(new ReturnRuleResponse("test", 10, 10, true)));
        assertThrows(ReturnRuleAlreadyExistException.class,
                () -> returnRuleService.createReturnRule(new ReturnRuleCreateRequest("test", 10, 10)));

        verify(returnRuleRepository, times(1)).findByReturnRuleName(anyString());
        verify(returnRuleNameRepository, never()).findById(anyString());
        verify(returnRuleRepository, never()).save(any());
        verify(returnRuleMapper, never()).mapToReturnRuleCreateResponse(any());
    }


    @Test
    @DisplayName("반품 규정 등록 실패 테스트(반품 규정 명이 존재하지 않음")
    void givneReturnCreateRequest_whenReturnRuleNameRepositoryFindById_thenThrowReturnRuleNameNotExistException() {
        when(returnRuleRepository.findByReturnRuleName(anyString())).thenReturn(Optional.empty());
        when(returnRuleNameRepository.findById(anyString())).thenThrow(ReturnRuleNameNotExistException.class);

        Assertions.assertThrows(ReturnRuleNameNotExistException.class, () -> returnRuleService.createReturnRule(new ReturnRuleCreateRequest("test", 10, 10)));

        verify(returnRuleRepository, times(1)).findByReturnRuleName(anyString());
        verify(returnRuleNameRepository, times(1)).findById(anyString());

    }

    @Test
    @DisplayName("반품 규정 수정 성공 테스트")
    void givenReturnRuleModifyRequest_whenModifyByReturnRule_thenReturnRetrunRuleModifyResponse() {
        String expectedBeforeName = "test123";
        ReturnRuleName expectedBeforeReturnRuleName = new ReturnRuleName(expectedBeforeName, LocalDate.of(1212, 11, 12));
        ReturnRule expectedBeforeReturnRule = new ReturnRule(1L, 1000, 10, true, LocalDate.of(1212, 12, 12), expectedBeforeReturnRuleName);

        String expectedName = "test";
        ReturnRuleName expectedReturnRuleName = new ReturnRuleName(expectedName, LocalDate.of(1212, 12, 12));
        ReturnRule expectedReturnRule = new ReturnRule(1L, 100, 10, true, LocalDate.of(1212, 12, 12), expectedReturnRuleName);
        ReturnRuleModifyResponse expectedReturnRuleModifyResponse = new ReturnRuleModifyResponse(expectedName, 100, 10, true);

        when(returnRuleRepository.findById(any())).thenReturn(Optional.of(expectedBeforeReturnRule));
        when(returnRuleNameRepository.findById(expectedName)).thenReturn(Optional.of(expectedReturnRuleName));
        when(returnRuleRepository.save(any(ReturnRule.class))).thenReturn(expectedReturnRule);
        when(returnRuleMapper.mapToReturnRuleModifyResponse(any())).thenReturn(expectedReturnRuleModifyResponse);

        ReturnRuleModifyResponse actual = returnRuleService.modifyReturnRule(new ReturnRuleModifyRequest(expectedName, 1000, 10), 1L);
        Assertions.assertEquals(expectedReturnRuleModifyResponse, actual);

        verify(returnRuleRepository, times(1)).findById(any());
        verify(returnRuleNameRepository, times(1)).findById(any());
        verify(returnRuleRepository, times(1)).save(any(ReturnRule.class));
        verify(returnRuleMapper, times(1)).mapToReturnRuleModifyResponse(any());
    }

    @Test
    @DisplayName("반품 규정 수정 실패 테스트(id에 대한 반품 규정이 존재하지 않을 때)")
    void givenReturnRuleModifyRequestAndLongId_whenReturnRuleRepositoryFindById_thenThrowReturnRuleNotExistException() {
        when(returnRuleRepository.findById(1L)).thenThrow(ReturnRuleNotExistException.class);

        ReturnRuleModifyRequest request = new ReturnRuleModifyRequest("test", 100, 10);
        Assertions.assertThrows(ReturnRuleNotExistException.class, () -> returnRuleService.modifyReturnRule(request, 1L));

        verify(returnRuleRepository, times(1)).findById(any());
        verify(returnRuleNameRepository, never()).findById(any());
        verify(returnRuleRepository, never()).save(any(ReturnRule.class));
        verify(returnRuleMapper, never()).mapToReturnRuleModifyResponse(any());
    }

    @Test
    @DisplayName("반품 규정 수정 실패 테스트(기존의 반품 규정 명이 존재하지 않을 때)")
    void givenReturnRuleModifyRequestAndLongId_whenReturnRuleNameRepositoryFindById_thenThrowReturnRuleNameNotExistException() {
        when(returnRuleRepository.findById(1L)).thenReturn(Optional.of(new ReturnRule()));
        when(returnRuleNameRepository.findById(any())).thenThrow(ReturnRuleNameNotExistException.class);
        ReturnRuleModifyRequest request = new ReturnRuleModifyRequest("test", 100, 10);
        Assertions.assertThrows(ReturnRuleNameNotExistException.class, () -> returnRuleService.modifyReturnRule(request, 1L));

        verify(returnRuleRepository, times(1)).findById(any());
        verify(returnRuleNameRepository, times(1)).findById(any());
        verify(returnRuleRepository, never()).save(any(ReturnRule.class));
        verify(returnRuleMapper, never()).mapToReturnRuleModifyResponse(any());
    }

    @Test
    @DisplayName("반품 규정 삭제 성공 테스트")
    void givenLongId_whenReturnRuleModifyIsAvailable_thenReturnReturnRuleDeleteResponse() {
        ReturnRuleName returnRuleName = new ReturnRuleName("test", LocalDate.of(1212, 12, 11));
        ReturnRule expectedReturnRule = new ReturnRule(1L, 100, 10, true, LocalDate.of(1212, 12, 12), returnRuleName);
        ReturnRuleDeleteResponse expected = new ReturnRuleDeleteResponse("OK");
        when(returnRuleRepository.findById(1L)).thenReturn(Optional.of(expectedReturnRule));

        ReturnRuleDeleteResponse actual = returnRuleService.deleteReturnRule(1L);
        assertEquals(expected.getResult(), actual.getResult());
    }

    @Test
    @DisplayName("반품 규정 삭제 실패 테스트(반품 규정이 존재 하지 않을 때)")
    void givenLongId_whenReturnRuleRepositoryFindById_thenThrowReturnRuleNotExistException() {
        when(returnRuleRepository.findById(any())).thenThrow(ReturnRuleNotExistException.class);
        assertThrows(ReturnRuleNotExistException.class, () -> returnRuleService.deleteReturnRule(1L));
    }
}