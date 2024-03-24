package store.mybooks.resource.returnrulename.repository;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;

/**
 * packageName    : store.mybooks.resource.returnrulename.repository<br>
 * fileName       : ReturnRuleNameRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 3/10/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/10/24        minsu11       최초 생성<br>
 */
@DataJpaTest
class ReturnRuleNameRepositoryTest {
    @Autowired
    ReturnRuleNameRepository returnRuleNameRepository;

    @BeforeEach
    void setUp() {
        ReturnRuleName returnRuleName1 = new ReturnRuleName("test1", LocalDate.of(1212, 12, 12));
        ReturnRuleName returnRuleName2 = new ReturnRuleName("test2", LocalDate.of(1212, 12, 12));

        returnRuleNameRepository.save(returnRuleName1);
        returnRuleNameRepository.save(returnRuleName2);
    }

    @Test
    @DisplayName("id로 반품규정 명 조회")
    void givenId_whenFindReturnRuleNameById_thenReturnOptionalReturnRuleNameResponse() {
        ReturnRuleNameResponse expected = new ReturnRuleNameResponse("test1", LocalDate.of(1212, 12, 12));
        ReturnRuleNameResponse actual = returnRuleNameRepository.findReturnRuleNameById("test1").orElse(null);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    @DisplayName("전체 반품 규정 명 목록 조회")
    void whenGetReturnRuleNameList_thenReturnRuleNameResponseList() {
        List<ReturnRuleNameResponse> expected = List.of(new ReturnRuleNameResponse("test1", LocalDate.of(1212, 12, 12)),
                new ReturnRuleNameResponse("test2", LocalDate.of(1212, 12, 12)));
        List<ReturnRuleNameResponse> actual = returnRuleNameRepository.getReturnRuleNameList();

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        Assertions.assertEquals(expected.get(1).getId(), actual.get(1).getId());


    }
}