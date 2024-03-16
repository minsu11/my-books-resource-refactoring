package store.mybooks.resource.delivery_rule.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule_name.repository.DeliveryRuleNameRepository;
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


    @BeforeEach
    void setUp() {
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName("test");
        DeliveryRule deliveryRule1 = new DeliveryRule(1, deliveryNameRule, "test1", 1000, 1000, LocalDate.of(2023,12,31), 1);
        DeliveryRule deliveryRule2 = new DeliveryRule(2, deliveryNameRule, "test2", 1000, 1000, LocalDate.of(2023,12,31), 1);
        deliveryRuleNameRepository.save(deliveryNameRule);

        deliveryRuleRepository.save(deliveryRule1);
        deliveryRuleRepository.save(deliveryRule2);
    }

    @Test
    @DisplayName("DeliveryRule List 조회 테스트")
    void given_whenGetDeliveryRuleList_thenReturnDeliveryRuleResponseList() {
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName("test");
        DeliveryRuleResponse deliveryRule1 = new DeliveryRuleResponse(1, deliveryNameRule.getId(), "test1", 1000, 1000, LocalDate.of(2023,12,31), 1);
        DeliveryRuleResponse deliveryRule2 = new DeliveryRuleResponse(2, deliveryNameRule.getId(), "test2", 1000, 1000, LocalDate.of(2023,12,31), 1);
        List<DeliveryRuleResponse> expected = List.of(deliveryRule1, deliveryRule2);
        List<DeliveryRuleResponse> result = deliveryRuleRepository.getDeliveryRuleList();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0).getId(), expected.get(0).getId());
        assertEquals(expected.get(1).getId(), expected.get(1).getId());
    }

}