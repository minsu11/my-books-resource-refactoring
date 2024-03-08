package store.mybooks.resource.point_rule_name.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.point_rule_name.dto.response.PointRuleNameResponse;
import store.mybooks.resource.point_rule_name.entity.PointRuleName;

/**
 * packageName    : store.mybooks.resource.point_rule_name.repository<br>
 * fileName       : PointRuleNameRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 3/8/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/8/24        minsu11       최초 생성<br>
 */
@DataJpaTest
class PointRuleNameRepositoryTest {
    @Autowired
    PointRuleNameRepository pointRuleNameRepository;

    @BeforeEach
    void setUp() {
        PointRuleName pointRuleName1 = new PointRuleName("test1");
        PointRuleName pointRuleName2 = new PointRuleName("test2");
        pointRuleNameRepository.save(pointRuleName1);
        pointRuleNameRepository.save(pointRuleName2);
    }

    @Test
    @DisplayName("id에 맞는 포인트 규정 명 조회")
    void givenId_whenGetPointRuleNameById_thenReturnOptionalPointRuleNameResponse() {
        String expected = "test1";
        Optional<PointRuleNameResponse> actual = pointRuleNameRepository.getPointRuleNameById(expected);
        Assertions.assertEquals(expected, actual.get().getId());
    }

    @Test
    @DisplayName("포인트 규정 명 전체 조회")
    void whenGetPointRuleList_thenReturnPointRuleNameResponseList() {
        List<PointRuleNameResponse> expected = List.of(new PointRuleNameResponse("test1"), new PointRuleNameResponse("test2"));
        List<PointRuleNameResponse> actual = pointRuleNameRepository.getPointRuleNameList();

        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        Assertions.assertEquals(expected.get(1).getId(), actual.get(1).getId());
    }

}