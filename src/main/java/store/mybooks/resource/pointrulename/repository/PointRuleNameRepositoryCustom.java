package store.mybooks.resource.pointrulename.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;

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
    /**
     * methodName : getPointRuleNameById<br>
     * author : minsu11<br>
     * description : {@code DB}에서 {@code id}에 맞는 포인트 명 조회.
     * <br> *
     *
     * @param id 조회할 {@code id}
     * @return optional
     */
    Optional<PointRuleNameResponse> getPointRuleNameById(String id);

    /**
     * methodName : getPointRuleNameList<br>
     * author : minsu11<br>
     * description : {@code DB}에 저장된 전체 포인트 명 조회.
     * <br> *
     *
     * @return list
     */
    List<PointRuleNameResponse> getPointRuleNameList();
}
