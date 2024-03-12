package store.mybooks.resource.returnrulename.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;

/**
 * packageName    : store.mybooks.resource.return_rule_name.repository<br>
 * fileName       : ReturnRuleNameRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface ReturnRuleNameRepositoryCustom {
    /**
     * methodName : findReturnRuleNameByName<br>
     * author : minsu11<br>
     * description : {@code id}에 대한 반품 규정 명 조회.
     * <br> *
     *
     * @param id 조회할 {@code id}
     * @return optional
     */
    Optional<ReturnRuleNameResponse> findReturnRuleNameById(String id);

    /**
     * methodName : getReturnRuleNameList<br>
     * author : minsu11<br>
     * description : 모든 반품 규정 명을 조회해서 {@code ReturnRuleNameResponse}로 반환.
     * <br> *
     *
     * @return {@code ReturnRuleNameResponse List}로 반환
     */
    List<ReturnRuleNameResponse> getReturnRuleNameList();
}
