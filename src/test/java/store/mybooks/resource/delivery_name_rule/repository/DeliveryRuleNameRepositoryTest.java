package store.mybooks.resource.delivery_name_rule.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.repository
 * fileName       : DeliveryNameRuleRepositoryTest
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
@DataJpaTest
class DeliveryRuleNameRepositoryTest {

    @Autowired
    private DeliveryRuleNameRepository deliveryRuleNameRepository;
    private static DeliveryRuleName deliveryNameRule;
    private static DeliveryRuleName saveDeliveryNameRule;

    @BeforeEach
    void setup() {
        deliveryNameRule = new DeliveryRuleName("test");
        saveDeliveryNameRule = deliveryRuleNameRepository.save(deliveryNameRule);
    }

    @Test
    @DisplayName("DeliveryRuleName read 테스트")
    void givenDeliveryRuleNameId_whenFindDeliveryRuleNameById_thenReturnDeliveryRuleNameDto() {
        Optional<DeliveryRuleNameDto> resultDto = deliveryRuleNameRepository.findDeliveryRuleNameById(
                saveDeliveryNameRule.getId());

        assertTrue(resultDto.isPresent());
        DeliveryRuleNameDto resultDeliveryNameRuleDto = resultDto.get();

        assertEquals(deliveryNameRule.getId(), resultDeliveryNameRuleDto.getId());
        assertEquals(deliveryNameRule.getCreatedDate(), resultDeliveryNameRuleDto.getCreatedDate());
    }
}