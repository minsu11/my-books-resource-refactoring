package store.mybooks.resource.returnrule.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.returnrule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.returnrule.entity.ReturnRule;

/**
 * packageName    : store.mybooks.resource.return_rule.repository<br>
 * fileName       : ReturnRuleRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 반품 규정에 대한 {@code query}문을 {@code query dsl}을
 * 이용해서 직접 선언해서 구현하는 interface.
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface ReturnRuleRepositoryCustom {
    /**
     * methodName : findByReturnRuleName<br>
     * author : minsu11<br>
     * description : {@code returnRuleName}로 조회된 반품 규정을
     * {@code Optional<ReturnRuleResponse>}로 반환해주는 query dsl 메서드
     * <br> *
     *
     * @param returnRuleName 조회할 반품 규정의 이름
     * @return optional
     */
    Optional<ReturnRuleResponse> findByReturnRuleName(String returnRuleName);

    /**
     * methodName : getReturnRuleResponseList<br>
     * author : minsu11<br>
     * description : 모든 반품 규정의 데이터를 {@code ReturnRuleResponse}로
     * 반환 해주는 query dsl로 정의된 메서드
     * <br> *
     *
     * @return list
     */
    List<ReturnRuleResponse> getReturnRuleResponseList();


    ReturnRule findByReturnRuleNameId(String returnRuleName);


}
