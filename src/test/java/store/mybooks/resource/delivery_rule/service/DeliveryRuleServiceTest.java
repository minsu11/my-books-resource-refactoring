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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameNotExistsException;
import store.mybooks.resource.delivery_rule_name.repository.DeliveryRuleNameRepository;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.mapper.DeliveryRuleMapper;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotExistsException;
import store.mybooks.resource.delivery_rule.repository.DeliveryRuleRepository;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

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
    @DisplayName("DeliveryRule 등록하는 테스트")
    void givenDeliveryRuleRegisterRequest_whenFindByIdAndSaveAndMapToResponse_thenCreateDeliveryRuleReturnDeliveryRuleResponse() {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test2", 2000, 2000);
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, "test2", 2000, 2000, deliveryRule.getCreatedDate(),
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
    void givenDeliveryRuleId_whenGetDeliveryRule_thenReturnDeliveryRuleDto() {
        DeliveryRuleDto expected = new DeliveryRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getDeliveryRuleName() {
                return "test";
            }

            @Override
            public String getCompanyName() {
                return "test";
            }

            @Override
            public Integer getCost() {
                return 1;
            }

            @Override
            public Integer getRuleCost() {
                return 1;
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }

            @Override
            public Integer getIsAvailable() {
                return 1;
            }
        };

        when(deliveryRuleRepository.findDeliveryRuleById(expected.getId())).thenReturn(Optional.of(expected));
        DeliveryRuleDto result = deliveryRuleService.getDeliveryRule(expected.getId());

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryRuleName(), result.getDeliveryRuleName());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).findDeliveryRuleById(expected.getId());
    }

    @Test
    @DisplayName("DeliveryRule 가 없을 경우 예외 테스트")
    void givenNotDeliveryRuleId_whenGetDeliveryRule_thenThrowDeliveryRuleNotFoundException() {
        DeliveryRuleDto deliveryRuleDto = new DeliveryRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getDeliveryRuleName() {
                return "test";
            }

            @Override
            public String getCompanyName() {
                return "test";
            }

            @Override
            public Integer getCost() {
                return 1;
            }

            @Override
            public Integer getRuleCost() {
                return 1;
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }

            @Override
            public Integer getIsAvailable() {
                return 1;
            }
        };

        when(deliveryRuleRepository.findDeliveryRuleById(deliveryRuleDto.getId())).thenReturn(Optional.empty());
        assertThrows(DeliveryRuleNotExistsException.class,
                () -> deliveryRuleService.getDeliveryRule(deliveryRuleDto.getId()));

        verify(deliveryRuleRepository, times(1)).findDeliveryRuleById(deliveryRuleDto.getId());

    }

    @Test
    @DisplayName("DeliveryRule 수정 정상 테스트")
    void givenDeliveryRuleIdAndDeliveryRuleModifyRequest_whenFindById_thenModifyDeliveryRuleReturnDeliveryRuleResponse() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest("test", "test2", 2000, 2000);
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, "test2", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.of(deliveryRule));
        when(deliveryRuleNameRepository.findById(deliveryRuleModifyRequest.getDeliveryNameRuleId())).thenReturn(
                Optional.of(deliveryNameRule));
        when(deliveryRuleMapper.mapToResponse(deliveryRule)).thenReturn(expected);

        DeliveryRuleResponse result =
                deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryRuleNameRepository, times(1)).findById(deliveryRuleModifyRequest.getDeliveryNameRuleId());
        verify(deliveryRuleMapper, times(1)).mapToResponse(deliveryRule);
    }

    @Test
    @DisplayName("DeliveryRule 이 없을 경우 예외 테스트")
    void givenNotDeliveryRuleModifyRequestAndDeliveryRuleId_whenModifyDeliveryRule_thenThrowDeliveryRuleNotFoundException() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest("test", "test2", 2000, 2000);
        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.empty());

        assertThrows(DeliveryRuleNotExistsException.class,
                () -> deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest));

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryRuleNameRepository, never()).findById(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    @DisplayName("DeliveryRuleName 이 없을 경우 예외 테스트")
    void givenNotDeliveryRuleModifyRequestAndDeliveryRuleId_whenModifyDeliveryRule_thenThrowDeliveryRuleNameNotFoundException() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest("test", "test2", 2000, 2000);

        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.of(deliveryRule));
        when(deliveryRuleNameRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DeliveryRuleNameNotExistsException.class,
                () -> deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest));

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryRuleNameRepository, times(1)).findById(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    @DisplayName("DeliveryRule 삭제 테스트")
    void givenDeliveryRuleId_whenDeleteDeliveryRule_thenDeleteDeliveryRuleReturnNothing() {
        Integer deliveryRuleId = 1;
        when(deliveryRuleRepository.existsById(deliveryRuleId)).thenReturn(true);
        deliveryRuleService.deleteDeliveryRule(deliveryRuleId);

        verify(deliveryRuleRepository, times(1)).existsById(deliveryRuleId);
        verify(deliveryRuleRepository, times(1)).deleteById(deliveryRuleId);
    }

    @Test
    @DisplayName("DeliveryRule가 없을 경우 테스트")
    void givenNotDeliveryRuleId_whenDeleteDeliveryRule_thenThrowDeliveryRuleNotFoundException() {
        Integer deliveryRuleId = 1;
        when(deliveryRuleRepository.existsById(deliveryRuleId)).thenReturn(false);
        assertThrows(DeliveryRuleNotExistsException.class, () -> deliveryRuleService.deleteDeliveryRule(deliveryRuleId));
        verify(deliveryRuleRepository, times(1)).existsById(deliveryRuleId);
        verify(deliveryRuleRepository, never()).deleteById(deliveryRuleId);
    }
}