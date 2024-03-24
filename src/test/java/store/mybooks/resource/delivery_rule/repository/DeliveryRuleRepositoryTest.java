package store.mybooks.resource.delivery_rule.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
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
    private TestEntityManager entityManager;

    @Autowired
    private DeliveryRuleRepository deliveryRuleRepository;

    DeliveryRule expected;

    @BeforeEach
    void setUp() {
        DeliveryRuleName deliveryRuleName = new DeliveryRuleName("test", LocalDate.now());
        entityManager.persistAndFlush(deliveryRuleName);

        DeliveryRule deliveryRule = new DeliveryRule(deliveryRuleName, "test1", 1000, 1000);
        expected = deliveryRuleRepository.save(deliveryRule);
    }

    @Test
    @DisplayName("DeliveryRule List 조회 테스트")
    void given_whenGetDeliveryRuleList_thenReturnDeliveryRuleResponseList() {
        List<DeliveryRuleResponse> result = deliveryRuleRepository.getDeliveryRuleList();

        assertEquals(1, result.size());
        DeliveryRuleResponse response = result.get(0);
        assertEquals("test1", response.getCompanyName());
        assertEquals(1000, response.getCost());
        assertEquals(1000, response.getRuleCost());
    }

    @Test
    @DisplayName("DeliveryRuleName id로 DeliveryRule 조회 테스트")
    void givenDeliveryRuleName_whenGetDeliveryRule_thenReturnDeliveryRuleResponse() {
        DeliveryRuleName deliveryRuleName = entityManager.find(DeliveryRuleName.class, "test");

        Optional<DeliveryRuleResponse> optionalResult =
                deliveryRuleRepository.getDeliveryRuleByName(deliveryRuleName.getId());

        assertTrue(optionalResult.isPresent());
        DeliveryRuleResponse result = optionalResult.get();
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCompanyName(), result.getCompanyName());
        assertEquals(expected.getCost(), result.getCost());
        assertEquals(expected.getRuleCost(), result.getRuleCost());
        assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        assertEquals(expected.getIsAvailable(), result.getIsAvailable());
    }
}