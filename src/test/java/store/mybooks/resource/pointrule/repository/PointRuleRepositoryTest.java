package store.mybooks.resource.pointrule.repository;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.pointrule.dto.response.PointRuleResponse;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.exception.PointRuleNotExistException;
import store.mybooks.resource.pointrulename.entity.PointRuleName;

/**
 * packageName    : store.mybooks.resource.point_rule.repository<br>
 * fileName       : PointRuleRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 3/9/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/9/24        minsu11       최초 생성<br>
 */
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
class PointRuleRepositoryTest {
    @Autowired
    private PointRuleRepository pointRuleRepository;
    @Autowired
    TestEntityManager testEntityManager;

    PointRule pointRule1;
    PointRule pointRule2;
    PointRuleName pointRuleName1;
    PointRuleName pointRuleName2;

    @BeforeEach
    void setUp() {
        pointRuleName1 = testEntityManager.persist(new PointRuleName("test1"));
        pointRuleName2 = testEntityManager.persist(new PointRuleName("test2"));

        pointRule1 = pointRuleRepository.save(new PointRule(1, pointRuleName1, 10, null, LocalDate.of(1212, 12, 12), true));
        pointRule2 = pointRuleRepository.save(new PointRule(2, pointRuleName2, null, 10, LocalDate.of(1212, 12, 12), false));
    }

    @Test
    @Order(1)
    @DisplayName("id에 맞는 포인트 규정 조회")
    void givenId_whenGetPointRuleById_thenReturnPointRuleResponseOptional() {
        PointRuleResponse actual = pointRuleRepository.getPointRuleById(pointRule1.getId()).orElse(null);
        assert actual != null;
        Assertions.assertEquals(pointRule1.getId(), actual.getId());
        Assertions.assertEquals("test1", actual.getPointRuleName());
        Assertions.assertEquals(pointRule1.getRate(), actual.getRate());

    }

    @Test
    @Order(2)
    @DisplayName("포인트 규정 목록 조회")
    void whenGetPointRuleList_thenReturnPointRuleResponseList() {
        List<PointRuleResponse> expected = List.of(new PointRuleResponse(3, "test1", 10, null));
        List<PointRuleResponse> actual = pointRuleRepository.getPointRuleList();
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getCost(), actual.get(0).getCost());
    }

    @Test
    @Order(3)
    @DisplayName("포인트 규정 명으로 포인트 규정 찾기")
    void givenPointRuleName_whenFindPointRuleByPointRuleName_thenReturnPointRule() {
        PointRuleName pointRuleName = new PointRuleName("test1");
        PointRule expected = new PointRule(1, pointRuleName, 10, null, LocalDate.of(1212, 12, 12), true);
        PointRule actual = pointRuleRepository.findPointRuleByPointRuleName("test1").orElse(null);

        Assertions.assertEquals(expected.getRate(), actual.getRate());
    }

    @Test
    @Order(4)
    @DisplayName("포인트 규정 페이징 테스트")
    void givenPageable_whenGetPointResponsePage_thenReturnPointRuleResponseList() {
        Pageable pageable = PageRequest.of(0, 2);
        List<PointRuleResponse> expected = List.of(new PointRuleResponse(3, "test1", 10, null));
        List<PointRuleResponse> actual = pointRuleRepository.getPointRuleResponsePage(pageable).getContent();

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getPointRuleName(), actual.get(0).getPointRuleName());

    }

    @Test
    @DisplayName("규정 명으로 포인트 규정 조회")
    void givenRuleName_whenGetPointRuleByName_thenReturnPointRuleResponse() {
        PointRuleResponse expected = new PointRuleResponse(1, pointRule1.getPointRuleName().getId(), 10, null);

        PointRuleResponse actual = pointRuleRepository.getPointRuleByName("test1")
                .orElseThrow(PointRuleNotExistException::new);
        Assertions.assertEquals(expected.getPointRuleName(), actual.getPointRuleName());
        Assertions.assertEquals(expected.getRate(), actual.getRate());

    }

}