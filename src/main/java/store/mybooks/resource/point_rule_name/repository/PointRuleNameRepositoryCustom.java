package store.mybooks.resource.point_rule_name.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.point_rule_name.dto.response.PointRuleNameResponse;

/**
 * packageName    : store.mybooks.resource.point_rule_name.repository<br>
 * fileName       : PointRuleNameRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface PointRuleNameRepositoryCustom {
    Optional<PointRuleNameResponse> getPointRuleNameById(String id);

    List<PointRuleNameResponse> getPointRuleNameList();
}
