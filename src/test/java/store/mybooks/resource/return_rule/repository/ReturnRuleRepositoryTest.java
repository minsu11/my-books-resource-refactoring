package store.mybooks.resource.return_rule.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.entity.ReturnRule;
import store.mybooks.resource.return_rule_name.entity.ReturnRuleName;
import store.mybooks.resource.return_rule_name.repository.ReturnRuleNameRepository;


/**
 * packageName    : store.mybooks.resource.return_rule.repository<br>
 * fileName       : ReturnRuleRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 2/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/26/24        minsu11       최초 생성<br>
 */
@DataJpaTest
class ReturnRuleRepositoryTest {

    @Autowired
    ReturnRuleRepository returnRuleRepository;
    @Autowired
    ReturnRuleNameRepository returnRuleNameRepository;


    @BeforeEach
    void setUp() {
        ReturnRuleName returnRuleName = new ReturnRuleName("test1", LocalDate.of(1212, 12, 12));
        returnRuleNameRepository.save(returnRuleName);

        ReturnRule returnRuleValue = new ReturnRule(1L, 10, 10, true, LocalDate.of(1212, 12, 12), returnRuleName);
        returnRuleRepository.save(returnRuleValue);
    }

    @Test
    @DisplayName("id에 대한 반품 규정 조회 성공 테스트")
    void findByReturnRuleNameId() {
        String expectedReturnName = "test1";
        int expectedDeliveryFee = 10;
        int expectedTerm = 10;

        Optional<ReturnRuleResponse> actual = returnRuleRepository.findByReturnRuleName("test1");
        Assertions.assertEquals(expectedReturnName, actual.get().getReturnName());
        Assertions.assertEquals(expectedDeliveryFee, actual.get().getDeliveryFee());
        Assertions.assertEquals(expectedTerm, actual.get().getTerm());
        Assertions.assertEquals(true, actual.get().getIsAvailable());
    }

    @Test
    @DisplayName("DB에 없는 반품 규정 id 조회 테스트")
    void givenReturnRuleId_whenFindByReturnRuleName_thenReturnEmptyData() {
        Assertions.assertThrows(NullPointerException.class, () -> returnRuleRepository.findByReturnRuleName("test2"));
    }

    @Test
    @DisplayName("DB에 저장된 모든 반품 규정 목록 조회")
    void given_whenGetReturnRuleResponseList_thenReturnReturnRuleResponseList() {
        List<ReturnRuleResponse> expected = List.of(new ReturnRuleResponse("test1", 10, 10, true));

        List<ReturnRuleResponse> actual = returnRuleRepository.getReturnRuleResponseList();
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getReturnName(), actual.get(0).getReturnName());
        Assertions.assertEquals(expected.get(0).getDeliveryFee(), actual.get(0).getDeliveryFee());
        Assertions.assertEquals(expected.get(0).getTerm(), actual.get(0).getTerm());
        Assertions.assertEquals(expected.get(0).getIsAvailable(), actual.get(0).getIsAvailable());
    }


}