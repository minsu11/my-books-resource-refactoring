package store.mybooks.resource.delivery_name_rule.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;

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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryNameRuleRepositoryTest {

    @Autowired
    private DeliveryNameRuleRepository deliveryNameRuleRepository;
    private static DeliveryNameRule deliveryNameRule;
    private static DeliveryNameRule saveDeliveryNameRule;

    @BeforeEach
    void setup() {
        deliveryNameRule = new DeliveryNameRule(1, "test", LocalDate.now());
        saveDeliveryNameRule = deliveryNameRuleRepository.save(deliveryNameRule);
    }

    @Test
    @Transactional
    void findDeliveryNameRuleByIdTest() {
        Optional<DeliveryNameRuleDto> resultDto = deliveryNameRuleRepository.findDeliveryNameRuleById(
                saveDeliveryNameRule.getId());

        assertTrue(resultDto.isPresent());
        DeliveryNameRuleDto resultDeliveryNameRuleDto = resultDto.get();

        assertEquals(deliveryNameRule.getId(), resultDeliveryNameRuleDto.getId());
        assertEquals(deliveryNameRule.getName(), resultDeliveryNameRuleDto.getName());
        assertEquals(deliveryNameRule.getCreatedDate(), resultDeliveryNameRuleDto.getCreatedDate());
    }
}