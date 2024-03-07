package store.mybooks.resource.point_rule.repository;

import java.util.List;
import java.util.Optional;
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
    /**
     * methodName : getPointRuleById<br>
     * author : minsu11<br>
     * description : id로 포인트 규정 조회.
     * <br> *
     *
     * @param id
     * @return optional
     */
    Optional<PointRuleResponse> getPointRuleById(Integer id);

    /**
     * methodName : getPointRuleList<br>
     * author : minsu11<br>
     * description : 포인트 규정 전체 조회.
     * <br> *
     *
     * @return list
     */
    List<PointRuleResponse> getPointRuleList();
}
