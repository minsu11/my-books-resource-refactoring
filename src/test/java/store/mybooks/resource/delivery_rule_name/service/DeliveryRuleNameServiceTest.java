package store.mybooks.resource.delivery_rule_name.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.delivery_rule_name.dto.mapper.DeliveryRuleNameMapper;
import store.mybooks.resource.delivery_rule_name.dto.request.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameAlreadyExistsException;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameNotExistsException;
import store.mybooks.resource.delivery_rule_name.repository.DeliveryRuleNameRepository;

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
@ExtendWith(MockitoExtension.class)
class DeliveryRuleNameServiceTest {
    @InjectMocks
    DeliveryRuleNameService deliveryRuleNameService;

    @Mock
    DeliveryRuleNameRepository deliveryRuleNameRepository;

    @Mock
    DeliveryRuleNameMapper deliveryRuleMapper;


    @Test
    @DisplayName("DeliveryRuleName 등록 테스트")
    void givenDeliveryRuleNameRegisterRequest_whenNormalCase_thenCreateAndSaveDeliveryRuleNameReturnDeliveryRuleNameResponse() {
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName("test");

        DeliveryRuleNameResponse expectedDeliveryRuleNameResponse = new DeliveryRuleNameResponse();
        expectedDeliveryRuleNameResponse.setId("test");
        expectedDeliveryRuleNameResponse.setCreatedDate(LocalDate.now());

        DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest = new DeliveryRuleNameRegisterRequest("test");
        when(deliveryRuleNameRepository.save(any())).thenReturn(deliveryNameRule);

        when(deliveryRuleMapper.mapToResponse(any())).thenReturn(expectedDeliveryRuleNameResponse);

        DeliveryRuleNameResponse result =
                deliveryRuleNameService.registerDeliveryNameRule(deliveryRuleNameRegisterRequest);

        assertEquals(expectedDeliveryRuleNameResponse.getId(), result.getId());
        assertEquals(expectedDeliveryRuleNameResponse.getCreatedDate(), result.getCreatedDate());

    }

    @Test
    @DisplayName("DeliveryRuleName이 이미 존재하는 경우 예외 테스트")
    void givenDeliveryRuleNameRegisterRequest_whenAlreadyExistsData_thenThrowDeliveryRuleNameAlreadyExistsException() {
        DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest = new DeliveryRuleNameRegisterRequest("test");
        when(deliveryRuleNameRepository.existsById(any())).thenReturn(true);

        assertThrows(DeliveryRuleNameAlreadyExistsException.class,
                () -> deliveryRuleNameService.registerDeliveryNameRule(deliveryRuleNameRegisterRequest));
    }

    @Test
    @DisplayName("DeliveryRuleName read 테스트")
    void givenDeliveryRuleNameId_whenFindDeliveryRUleNameById_thenReturnDeliveryRuleNameDto() {
        DeliveryRuleNameDto expectedDeliveryNameRuleDto = new DeliveryRuleNameDto() {
            @Override
            public String getId() {
                return "test";
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }
        };

        when(deliveryRuleNameRepository.findDeliveryRuleNameById("test")).thenReturn(
                Optional.of(expectedDeliveryNameRuleDto));

        DeliveryRuleNameDto result = deliveryRuleNameService.getDeliveryNameRule("test");

        assertEquals(expectedDeliveryNameRuleDto.getId(), result.getId());
        assertEquals(expectedDeliveryNameRuleDto.getCreatedDate(), result.getCreatedDate());
    }

    @Test
    @DisplayName("DeliveryRuleName 없을 경우 예외 테스트")
    void givenNotDeliveryRuleNameId_whenFindDeliveryRuleNameById_thenThrowDeliveryRuleNameNotFoundException() {
        when(deliveryRuleNameRepository.findDeliveryRuleNameById("test")).thenReturn(Optional.empty());
        assertThrows(DeliveryRuleNameNotExistsException.class,
                () -> deliveryRuleNameService.getDeliveryNameRule("test"));
    }


    @Test
    @DisplayName("DeliveryRuleName 삭제 테스트")
    void givenDeliveryRuleNameId_whenExistsByIdTrueAndDeleteDeliveryNameRule_thenDeleteDeliveryRuleNameReturnNothing() {
        String id = "test";

        when(deliveryRuleNameRepository.existsById(id)).thenReturn(true);
        deliveryRuleNameService.deleteDeliveryNameRule(id);


        verify(deliveryRuleNameRepository, times(1)).existsById(id);

        verify(deliveryRuleNameRepository, times(1)).deleteById(id);

    }

    @Test
    @DisplayName("DeliveryRuleName 이 없을 경우 예외 테스트")
    void givenNotDeliveryRuleNameId_whenExistsByIdFalse_thenThrowDeliveryRuleNameNotFoundException() {
        String id = "test";
        when(deliveryRuleNameRepository.existsById(id)).thenReturn(false);

        assertThrows(DeliveryRuleNameNotExistsException.class,
                () -> deliveryRuleNameService.deleteDeliveryNameRule(id));
        verify(deliveryRuleNameRepository, times(1)).existsById(id);
        verify(deliveryRuleNameRepository, never()).deleteById(id);
    }


}