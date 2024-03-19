package store.mybooks.resource.delivery_rule_name.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.entity.QDeliveryRuleName;

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

    @BeforeEach
    void setup() {
        DeliveryRuleName deliveryRuleName1 = new DeliveryRuleName("test1", LocalDate.of(2023, 12, 31));
        DeliveryRuleName deliveryRuleName2 = new DeliveryRuleName("test2", LocalDate.of(2023, 12, 31));
        deliveryRuleNameRepository.save(deliveryRuleName1);
        deliveryRuleNameRepository.save(deliveryRuleName2);
    }

    @Test
    @DisplayName("DeliveryRuleName read 테스트")
    void givenDeliveryRuleNameId_whenFindDeliveryRuleNameById_thenReturnDeliveryRuleNameDto() {
        DeliveryRuleNameDto expected = new DeliveryRuleNameDto() {
            @Override
            public String getId() {
                return "test1";
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.of(2023, 12, 31);
            }
        };
        Optional<DeliveryRuleNameDto> resultDto = deliveryRuleNameRepository.findDeliveryRuleNameById(
                expected.getId());

        assertTrue(resultDto.isPresent());
        DeliveryRuleNameDto resultDeliveryNameRuleDto = resultDto.get();

        assertEquals(expected.getId(), resultDeliveryNameRuleDto.getId());
        assertEquals(expected.getCreatedDate(), resultDeliveryNameRuleDto.getCreatedDate());
    }

    @Test
    @DisplayName("배송 규정 이름 목록 조회")
    void given_when_thenReturnDeliveryRuleNameList() {
        List<DeliveryRuleNameResponse> expected = List.of(
                new DeliveryRuleNameResponse("test1", LocalDate.of(2023, 12, 31)),
                new DeliveryRuleNameResponse("test2", LocalDate.of(2023, 12, 31))
        );

        List<DeliveryRuleNameResponse> result = deliveryRuleNameRepository.getDeliveryRuleNameList();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0).getId(), expected.get(0).getId());
        assertEquals(expected.get(1).getId(), expected.get(1).getId());
    }
}