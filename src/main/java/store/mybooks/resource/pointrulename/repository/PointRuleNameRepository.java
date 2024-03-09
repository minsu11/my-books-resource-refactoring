package store.mybooks.resource.pointrulename.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.pointrulename.entity.PointRuleName;

/**
 * packageName    : store.mybooks.resource.point_rule_name.repository<br>
 * fileName       : PointRuleNameRepository<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
public interface PointRuleNameRepository extends JpaRepository<PointRuleName, String>, PointRuleNameRepositoryCustom {

}
