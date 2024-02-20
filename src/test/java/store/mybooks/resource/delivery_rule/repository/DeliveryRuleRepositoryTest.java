package store.mybooks.resource.delivery_rule.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryRuleNameRepository;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_rule.repository
 * fileName       : DeliveryRuleRepositoryTest
 * author         : Fiat_lux
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        Fiat_lux       최초 생성
 */
@DataJpaTest
class DeliveryRuleRepositoryTest {

    @Autowired
    private DeliveryRuleRepository deliveryRuleRepository;

    @Autowired
    private DeliveryRuleNameRepository deliveryRuleNameRepository;

    private static DeliveryRuleDto expected;


    @BeforeEach
    void setUp() {
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName("test");
        DeliveryRule deliveryRule = new DeliveryRule(1, deliveryNameRule, "test", 1000, 1000, LocalDate.now(), 1);
        deliveryRuleNameRepository.save(deliveryNameRule);

        expected = new DeliveryRuleDto() {
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
                return 1000;
            }

            @Override
            public Integer getRuleCost() {
                return 1000;
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

        deliveryRuleRepository.save(deliveryRule);
    }

    @Test
    @DisplayName("DeliveryRule 찾는 테스트")
    void givenDeliveryRuleId_whenFindDeliveryRuleById_thenReturnDeliveryRuleDto() {
        Optional<DeliveryRuleDto> optionalResult =
                deliveryRuleRepository.findDeliveryRuleById(expected.getId());

        assertTrue(optionalResult.isPresent());
        DeliveryRuleDto result = optionalResult.get();

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());
    }

}