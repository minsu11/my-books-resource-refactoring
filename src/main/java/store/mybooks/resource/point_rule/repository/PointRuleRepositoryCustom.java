package store.mybooks.resource.point_rule.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.point_rule.dto.response.PointRuleResponse;

/**
 * packageName    : store.mybooks.resource.point_rule.repository<br>
 * fileName       : PointRuleRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface PointRuleRepositoryCustom {
    PointRuleResponse getPointRuleById(Integer id);
}
