package store.mybooks.resource.delivery_rule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.delivery_rule.dto.mapper.DeliveryRuleMapper;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotExistsException;
import store.mybooks.resource.delivery_rule.repository.DeliveryRuleRepository;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameNotExistsException;
import store.mybooks.resource.delivery_rule_name.repository.DeliveryRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.delivery_rule.service
 * fileName       : DeliveryRuleServiceTest
 * author         : Fiat_lux
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        Fiat_lux       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class DeliveryRuleServiceTest {
    @InjectMocks
    DeliveryRuleService deliveryRuleService;
    @Mock
    DeliveryRuleRepository deliveryRuleRepository;
    @Mock
    DeliveryRuleNameRepository deliveryRuleNameRepository;
    @Mock
    DeliveryRuleMapper deliveryRuleMapper;

    static DeliveryRuleName deliveryNameRule;
    static DeliveryRule deliveryRule;

    @BeforeEach
    void setUp() {
        deliveryNameRule = new DeliveryRuleName("test");
        deliveryRule = new DeliveryRule(1, deliveryNameRule, "test", 1000, 1000, LocalDate.now(), 1);
    }

    @Test
    @DisplayName("deliveryRule list 조회하는 테스트")
    void given_whenGetDeliveryRuleList_thenReturnDeliveryRuleResponseList() {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "test", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());
        List<DeliveryRuleResponse> expected = List.of(deliveryRuleResponse);
        when(deliveryRuleRepository.getDeliveryRuleList()).thenReturn(expected);
        List<DeliveryRuleResponse> result = deliveryRuleService.getDeliveryRuleList();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
        assertEquals(expected.get(0).getDeliveryRuleNameId(), result.get(0).getDeliveryRuleNameId());
        assertEquals(expected.get(0).getCompanyName(), result.get(0).getCompanyName());
        assertEquals(expected.get(0).getCost(), result.get(0).getCost());
        assertEquals(expected.get(0).getRuleCost(), result.get(0).getRuleCost());
        assertEquals(expected.get(0).getCreatedDate(), result.get(0).getCreatedDate());
        assertEquals(expected.get(0).getIsAvailable(), result.get(0).getIsAvailable());
    }

    @Test
    @DisplayName("DeliveryRule 등록하는 테스트")
    void givenDeliveryRuleRegisterRequest_whenFindByIdAndSaveAndMapToResponse_thenCreateDeliveryRuleReturnDeliveryRuleResponse() {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test", 2000, 2000);
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, "test", "test", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryRuleNameRepository.findById(any())).thenReturn(Optional.of(deliveryNameRule));
        when(deliveryRuleRepository.save(any())).thenReturn(deliveryRule);
        when(deliveryRuleMapper.mapToResponse(any())).thenReturn(expected);
        DeliveryRuleResponse result =
                deliveryRuleService.registerDeliveryRule(deliveryRuleRegisterRequest);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleNameRepository, times(1)).findById(any());
        verify(deliveryRuleRepository, times(1)).save(any());
        verify(deliveryRuleMapper, times(1)).mapToResponse(any());
    }

    @Test
    @DisplayName("DeliveryRuleName 이 없을 경우 예외 테스트")
    void givenDeliveryRuleRegister_whenFindByIdNull_thenThrowDeliveryRuleNameNotFoundException() {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test2", 2000, 2000);
        when(deliveryRuleNameRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DeliveryRuleNameNotExistsException.class,
                () -> deliveryRuleService.registerDeliveryRule(deliveryRuleRegisterRequest));

        verify(deliveryRuleNameRepository, times(1)).findById(any());
        verify(deliveryRuleRepository, never()).save(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    @DisplayName("DeliveryRule read 테스트")
    void givenDeliveryRuleId_whenGetDeliveryRule_thenReturnDeliveryRuleResponse() {
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, "test", "test", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryRuleRepository.findById(expected.getId())).thenReturn(Optional.of(deliveryRule));
        when(deliveryRuleMapper.mapToResponse(any(DeliveryRule.class))).thenReturn(expected);
        DeliveryRuleResponse result = deliveryRuleService.getDeliveryRule(expected.getId());

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryRuleNameId(), result.getDeliveryRuleNameId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).findById(expected.getId());

    }

    @Test
    @DisplayName("DeliveryRuleNameId 로 DeliveryRule 조회하는 테스트")
    void givenDeliveryRuleNameId_whenGetDeliveryRuleByName_thenReturnDeliveryRuleResponse() {
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, "test", "test", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryRuleRepository.getDeliveryRuleByName(any(String.class))).thenReturn(Optional.of(expected));
        DeliveryRuleResponse result =
                deliveryRuleService.getDeliveryRuleByName(expected.getDeliveryRuleNameId());

        assertNotNull(result);
        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryRuleNameId(), result.getDeliveryRuleNameId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).getDeliveryRuleByName(expected.getDeliveryRuleNameId());
    }

    @Test
    @DisplayName("DeliveryRuleId로 DeliveryRule 이 없을 경우 예외 테스트")
    void givenNotDeliveryRuleNameID_whenGetDeliveryRuleByName_thenThrowDeliveryRuleNotExistsException() {
        when(deliveryRuleRepository.getDeliveryRuleByName(any(String.class))).thenReturn(Optional.empty());
        assertThrows(DeliveryRuleNotExistsException.class,
                () -> deliveryRuleService.getDeliveryRuleByName("hi"));

        verify(deliveryRuleRepository, times(1)).getDeliveryRuleByName("hi");
    }

    @Test
    @DisplayName("DeliveryRule 가 없을 경우 예외 테스트")
    void givenNotDeliveryRuleId_whenGetDeliveryRule_thenThrowDeliveryRuleNotFoundException() {

        when(deliveryRuleRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(DeliveryRuleNotExistsException.class,
                () -> deliveryRuleService.getDeliveryRule(1));

        verify(deliveryRuleRepository, times(1)).findById(deliveryRule.getId());

    }

    @Test
    @DisplayName("DeliveryRule 수정 정상 테스트")
    void givenDeliveryRuleIdAndDeliveryRuleModifyRequest_whenFindById_thenModifyDeliveryRuleReturnDeliveryRuleResponse() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest(1, "test2", 2000, 2000);
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, "test2", "test2", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.of(deliveryRule));
        when(deliveryRuleMapper.mapToResponse(any(DeliveryRule.class))).thenReturn(expected);

        DeliveryRuleResponse result =
                deliveryRuleService.modifyDeliveryRule(deliveryRuleModifyRequest);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryRuleMapper, times(1)).mapToResponse(deliveryRule);
    }

    @Test
    @DisplayName("DeliveryRule 이 없을 경우 예외 테스트")
    void givenNotDeliveryRuleModifyRequestAndDeliveryRuleId_whenModifyDeliveryRule_thenThrowDeliveryRuleNotFoundException() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest(1, "test2", 2000, 2000);
        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.empty());

        assertThrows(DeliveryRuleNotExistsException.class,
                () -> deliveryRuleService.modifyDeliveryRule(deliveryRuleModifyRequest));

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryRuleNameRepository, never()).findById(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    @DisplayName("DeliveryRule 약삭제 테스트")
    void givenDeliveryRuleId_whenDeleteDeliveryRule_thenDeleteDeliveryRuleReturnNothing() {
        Integer deliveryRuleId = 1;
        Integer deleteDeliveryRuleId = 0;
        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.of(deliveryRule));
        assertEquals(deliveryRule.getIsAvailable(), deliveryRuleId);
        deliveryRuleService.deleteDeliveryRule(deliveryRuleId);
        assertEquals(deliveryRule.getIsAvailable(), deleteDeliveryRuleId);
    }

    @Test
    @DisplayName("DeliveryRule가 없을 경우 테스트")
    void givenNotDeliveryRuleId_whenDeleteDeliveryRule_thenThrowDeliveryRuleNotFoundException() {
        Integer deliveryRuleId = 1;
        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.empty());
        assertThrows(DeliveryRuleNotExistsException.class,
                () -> deliveryRuleService.deleteDeliveryRule(deliveryRuleId));
        assertEquals(deliveryRule.getIsAvailable(), deliveryRuleId);
    }
}