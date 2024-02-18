package store.mybooks.resource.delivery_rule.repository;

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
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;

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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRuleRepositoryTest {

    @Autowired
    private DeliveryRuleRepository deliveryRuleRepository;

    private static DeliveryRuleDto expected;


    @BeforeEach
    void setUp() {
        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(1, "test", LocalDate.now());
        DeliveryRule deliveryRule = new DeliveryRule(1, deliveryNameRule, "test", 1000, 1000, LocalDate.now(), true);

        expected = new DeliveryRuleDto() {
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
            public Boolean getIsAvailable() {
                return true;
            }
        };

        deliveryRuleRepository.save(deliveryRule);
    }

    @Test
    @Transactional
    void findDeliveryRuleByIdTest() {
        Optional<DeliveryRuleDto> optionalResult =
                deliveryRuleRepository.findDeliveryRuleById(expected.getId());

        assertTrue(optionalResult.isPresent());
        DeliveryRuleDto result = optionalResult.get();

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDeliveryNameRuleId(), result.getDeliveryNameRuleId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());
    }

}