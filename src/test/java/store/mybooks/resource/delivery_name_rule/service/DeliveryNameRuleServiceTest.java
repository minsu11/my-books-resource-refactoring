package store.mybooks.resource.delivery_name_rule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleModifyRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleRegisterRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleResponse;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_name_rule.exception.DeliveryNameRuleNotFoundException;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryNameRuleRepository;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleMapper;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.service
 * fileName       : DeliveryNameRuleServiceTest
 * author         : Fiat_lux
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        Fiat_lux       최초 생성
 */
@SpringBootTest
class DeliveryNameRuleServiceTest {
    @Autowired
    DeliveryNameRuleService deliveryNameRuleService;

    @MockBean
    DeliveryNameRuleRepository deliveryNameRuleRepository;

    @MockBean
    DeliveryRuleMapper deliveryRuleMapper;


    @Test
    void registerDeliveryNameRuleTest() {
        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(1, "test", LocalDate.now());

        DeliveryNameRuleResponse expectedDeliveryNameRuleResponse = new DeliveryNameRuleResponse();
        expectedDeliveryNameRuleResponse.setId(1);
        expectedDeliveryNameRuleResponse.setName("test");
        expectedDeliveryNameRuleResponse.setCreatedDate(LocalDate.now());

        DeliveryNameRuleRegisterRequest deliveryNameRuleRegisterRequest = new DeliveryNameRuleRegisterRequest("test");
        when(deliveryNameRuleRepository.save(any())).thenReturn(deliveryNameRule);

        DeliveryNameRuleResponse result =
                deliveryNameRuleService.registerDeliveryNameRule(deliveryNameRuleRegisterRequest);

        assertEquals(expectedDeliveryNameRuleResponse.getId(), result.getId());
        assertEquals(expectedDeliveryNameRuleResponse.getName(), result.getName());
        assertEquals(expectedDeliveryNameRuleResponse.getCreatedDate(), result.getCreatedDate());

    }

    @Test
    void getDeliveryNameRule() {
        DeliveryNameRuleDto expectedDeliveryNameRuleDto = new DeliveryNameRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }
        };

        Integer id = 1;

        when(deliveryNameRuleRepository.findDeliveryNameRuleById(id)).thenReturn(
                Optional.of(expectedDeliveryNameRuleDto));

        DeliveryNameRuleDto result = deliveryNameRuleService.getDeliveryNameRule(id);

        assertEquals(expectedDeliveryNameRuleDto.getId(), result.getId());
        assertEquals(expectedDeliveryNameRuleDto.getName(), result.getName());
        assertEquals(expectedDeliveryNameRuleDto.getCreatedDate(), result.getCreatedDate());
    }

    @Test
    void getDeliveryNameRuleNotFoundTest() {
        Integer id = 1;
        when(deliveryNameRuleRepository.findDeliveryNameRuleById(id)).thenReturn(Optional.empty());
        assertThrows(DeliveryNameRuleNotFoundException.class, () -> deliveryNameRuleService.getDeliveryNameRule(id));
    }

    @Test
    void modifyDeliveryNameRuleTest() {
        Integer deliveryNameRuleId = 1;
        DeliveryNameRuleModifyRequest deliveryNameRuleModifyRequest = new DeliveryNameRuleModifyRequest("update");
        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(deliveryNameRuleId, "test", LocalDate.now());

        DeliveryNameRuleResponse expected = new DeliveryNameRuleResponse();
        expected.setId(deliveryNameRuleId);
        expected.setName("update");
        expected.setCreatedDate(deliveryNameRule.getCreatedDate());

        when(deliveryNameRuleRepository.findById(any())).thenReturn(Optional.of(deliveryNameRule));
        DeliveryNameRuleResponse result =
                deliveryNameRuleService.modifyDeliveryNameRule(deliveryNameRuleId, deliveryNameRuleModifyRequest);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
    }

    @Test
    void modifyDeliveryNameRuleNotFoundTest() {
        Integer deliveryNameRuleId = 1;
        DeliveryNameRuleModifyRequest deliveryNameRuleModifyRequest = new DeliveryNameRuleModifyRequest("update");
        when(deliveryNameRuleRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(DeliveryNameRuleNotFoundException.class,
                () -> deliveryNameRuleService.modifyDeliveryNameRule(deliveryNameRuleId,
                        deliveryNameRuleModifyRequest));
    }

    @Test
    void deleteDeliveryNameRuleTest() {
        Integer id = 1;

        when(deliveryNameRuleRepository.existsById(id)).thenReturn(true);
        deliveryNameRuleService.deleteDeliveryNameRule(id);


        verify(deliveryNameRuleRepository, times(1)).existsById(id);

        verify(deliveryNameRuleRepository, times(1)).deleteById(id);

    }

    @Test
    void deleteDeliveryNameRuleNotFoundTest() {
        Integer id = 1;
        when(deliveryNameRuleRepository.existsById(id)).thenReturn(false);

        assertThrows(DeliveryNameRuleNotFoundException.class, () -> deliveryNameRuleService.deleteDeliveryNameRule(id));
        verify(deliveryNameRuleRepository, times(1)).existsById(id);
        verify(deliveryNameRuleRepository, never()).deleteById(id);
    }


}