package store.mybooks.resource.delivery_rule_name.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;

/**
 * packageName    : store.mybooks.resource.delivery_rule_name.repository <br/>
 * fileName       : DeliveryRuleNameRepositoryCustom<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/10/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/10/24        Fiat_lux       최초 생성<br/>
 */
@NoRepositoryBean
public interface DeliveryRuleNameRepositoryCustom {
    List<DeliveryRuleNameResponse> getDeliveryRuleNameList();
}
