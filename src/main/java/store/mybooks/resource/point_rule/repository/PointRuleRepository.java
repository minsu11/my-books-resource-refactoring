package store.mybooks.resource.point_rule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.point_rule.entity.PointRule;

/**
 * packageName    : store.mybooks.resource.point_rule.repository<br>
 * fileName       : PointRuleRepository<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */

public interface PointRuleRepository extends JpaRepository<PointRule, Integer>, PointRuleRepositoryCustom {

}
