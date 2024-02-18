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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_name_rule.exception.DeliveryNameRuleNotFoundException;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryNameRuleRepository;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleMapper;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotFoundException;
import store.mybooks.resource.delivery_rule.repository.DeliveryRuleRepository;

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
@SpringBootTest
class DeliveryRuleServiceTest {
    @Autowired
    DeliveryRuleService deliveryRuleService;
    @MockBean
    DeliveryRuleRepository deliveryRuleRepository;
    @MockBean
    DeliveryNameRuleRepository deliveryNameRuleRepository;
    @MockBean
    DeliveryRuleMapper deliveryRuleMapper;

    static DeliveryNameRule deliveryNameRule;
    static DeliveryRule deliveryRule;

    @BeforeEach
    void setUp() {
        deliveryNameRule = new DeliveryNameRule(1, "test", LocalDate.now());
        deliveryRule = new DeliveryRule(1, deliveryNameRule, "test", 1000, 1000, LocalDate.now(), true);
    }

    @Test
    void registerDeliveryRuleTest() {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest(1, "test2", 2000, 2000);
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, deliveryNameRule, "test2", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryNameRuleRepository.findById(any())).thenReturn(Optional.of(deliveryNameRule));
        when(deliveryRuleRepository.save(any())).thenReturn(deliveryRule);
        when(deliveryRuleMapper.mapToResponse(any())).thenReturn(expected);
        DeliveryRuleResponse result =
                deliveryRuleService.registerDeliveryRule(deliveryRuleRegisterRequest);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryNameRule(), result.getDeliveryNameRule());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryNameRuleRepository, times(1)).findById(any());
        verify(deliveryRuleRepository, times(1)).save(any());
        verify(deliveryRuleMapper, times(1)).mapToResponse(any());
    }

    @Test
    void registerDeliveryRuleNotFoundTest() {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest(1, "test2", 2000, 2000);
        when(deliveryNameRuleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DeliveryNameRuleNotFoundException.class,
                () -> deliveryRuleService.registerDeliveryRule(deliveryRuleRegisterRequest));

        verify(deliveryNameRuleRepository, times(1)).findById(any());
        verify(deliveryRuleRepository, never()).save(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    void getDeliveryRuleTest() {
        DeliveryRuleDto expected = new DeliveryRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public Integer getDeliveryNameRuleId() {
                return 1;
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
            public Boolean getIsAvailable() {
                return true;
            }
        };

        when(deliveryRuleRepository.findDeliveryRuleById(expected.getId())).thenReturn(Optional.of(expected));
        DeliveryRuleDto result = deliveryRuleService.getDeliveryRule(expected.getId());

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryNameRuleId(), result.getDeliveryNameRuleId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).findDeliveryRuleById(expected.getId());
    }

    @Test
    void getDeliveryRuleNotFoundTest() {
        DeliveryRuleDto deliveryRuleDto = new DeliveryRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public Integer getDeliveryNameRuleId() {
                return 1;
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
            public Boolean getIsAvailable() {
                return true;
            }
        };

        when(deliveryRuleRepository.findDeliveryRuleById(deliveryRuleDto.getId())).thenReturn(Optional.empty());
        assertThrows(DeliveryRuleNotFoundException.class,
                () -> deliveryRuleService.getDeliveryRule(deliveryRuleDto.getId()));

        verify(deliveryRuleRepository, times(1)).findDeliveryRuleById(deliveryRuleDto.getId());

    }

    @Test
    void modifyDeliveryRuleTest() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest = new DeliveryRuleModifyRequest(1, "test2", 2000, 2000);
        DeliveryRuleResponse expected =
                new DeliveryRuleResponse(1, deliveryNameRule, "test2", 2000, 2000, deliveryRule.getCreatedDate(),
                        deliveryRule.getIsAvailable());

        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.of(deliveryRule));
        when(deliveryNameRuleRepository.findById(deliveryRuleModifyRequest.getDeliveryNameRuleId())).thenReturn(
                Optional.of(deliveryNameRule));
        when(deliveryRuleMapper.mapToResponse(deliveryRule)).thenReturn(expected);

        DeliveryRuleResponse result =
                deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryNameRule(), result.getDeliveryNameRule());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryNameRuleRepository, times(1)).findById(deliveryRuleModifyRequest.getDeliveryNameRuleId());
        verify(deliveryRuleMapper, times(1)).mapToResponse(deliveryRule);
    }

    @Test
    void modifyDeliveryRuleNotFoundDeliveryRuleTest() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest = new DeliveryRuleModifyRequest(1, "test2", 2000, 2000);
        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.empty());

        assertThrows(DeliveryRuleNotFoundException.class,
                () -> deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest));

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryNameRuleRepository, times(1)).findById(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    void modifyDeliveryRuleNotFoundDeliveryNameRuleTest() {
        int deliveryRuleId = 1;
        DeliveryRuleModifyRequest deliveryRuleModifyRequest = new DeliveryRuleModifyRequest(1, "test2", 2000, 2000);

        when(deliveryRuleRepository.findById(deliveryRuleId)).thenReturn(Optional.of(deliveryRule));
        when(deliveryNameRuleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DeliveryRuleNotFoundException.class,
                () -> deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest));

        verify(deliveryRuleRepository, times(1)).findById(deliveryRuleId);
        verify(deliveryNameRuleRepository, times(1)).findById(any());
        verify(deliveryRuleMapper, never()).mapToResponse(any());
    }

    @Test
    void deleteDeliveryRuleTest() {
        Integer deliveryRuleId = 1;
        when(deliveryRuleRepository.existsById(deliveryRuleId)).thenReturn(true);
        deliveryRuleService.deleteDeliveryRule(deliveryRuleId);

        verify(deliveryRuleRepository, times(1)).existsById(deliveryRuleId);
        verify(deliveryRuleRepository, times(1)).deleteById(deliveryRuleId);
    }

    @Test
    void deleteDeliveryRuleNotFoundTest() {
        Integer deliveryRuleId = 1;
        when(deliveryRuleRepository.existsById(deliveryRuleId)).thenReturn(false);
        assertThrows(DeliveryRuleNotFoundException.class, () -> deliveryRuleService.deleteDeliveryRule(deliveryRuleId));
        verify(deliveryRuleRepository, times(1)).existsById(deliveryRuleId);
        verify(deliveryRuleRepository, never()).deleteById(deliveryRuleId);
    }
}