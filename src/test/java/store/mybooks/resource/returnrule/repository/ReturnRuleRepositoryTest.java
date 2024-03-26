package store.mybooks.resource.returnrule.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.returnrule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.returnrule.entity.ReturnRule;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;
import store.mybooks.resource.returnrulename.repository.ReturnRuleNameRepository;


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
        ReturnRuleName returnRuleName1 = new ReturnRuleName("test", LocalDate.of(1212, 12, 12));
        returnRuleNameRepository.save(returnRuleName1);

        ReturnRule returnRuleValue1 = new ReturnRule(1, 10, 10, false, LocalDate.of(1212, 12, 12), returnRuleName1);
        returnRuleRepository.save(returnRuleValue1);

        ReturnRuleName returnRuleName2 = new ReturnRuleName("test1", LocalDate.of(1212, 12, 12));
        returnRuleNameRepository.save(returnRuleName2);

        ReturnRule returnRuleValue2 = new ReturnRule(2, 10, 10, true, LocalDate.of(1212, 12, 12), returnRuleName2);
        returnRuleRepository.save(returnRuleValue2);
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
        List<ReturnRuleResponse> expected = List.of(new ReturnRuleResponse(1, "test1", 10, 10, true));

        List<ReturnRuleResponse> actual = returnRuleRepository.getReturnRuleResponseList();
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getReturnName(), actual.get(0).getReturnName());
        Assertions.assertEquals(expected.get(0).getDeliveryFee(), actual.get(0).getDeliveryFee());
        Assertions.assertEquals(expected.get(0).getTerm(), actual.get(0).getTerm());
        Assertions.assertEquals(expected.get(0).getIsAvailable(), actual.get(0).getIsAvailable());
    }

    @Test
    @DisplayName("반품 규정 명을 이용한 반품 규정 조회 ")
    void findByReturnRuleNameIdTest() {
        ReturnRuleName returnRuleName = new ReturnRuleName("test1", LocalDate.of(1212, 12, 12));
        ReturnRule expected = new ReturnRule(6, 10, 10, true, LocalDate.of(1212, 12, 12), returnRuleName);
        ReturnRule result = returnRuleRepository.findByReturnRuleNameId(returnRuleName.getId());

        Assertions.assertEquals(expected.getDeliveryFee(), result.getDeliveryFee());
        Assertions.assertEquals(expected.getTerm(), result.getTerm());
        Assertions.assertEquals(expected.getIsAvailable(), result.getIsAvailable());
        Assertions.assertEquals(expected.getCreatedDate(), result.getCreatedDate());
        Assertions.assertEquals(expected.getReturnRuleName().getId(), result.getReturnRuleName().getId());
        Assertions.assertEquals(expected.getReturnRuleName().getCreatedDate(),
                result.getReturnRuleName().getCreatedDate());


    }


}