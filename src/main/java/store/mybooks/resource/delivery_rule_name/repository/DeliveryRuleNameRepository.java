package store.mybooks.resource.delivery_rule_name.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.repository<br/>
 * fileName       : DeliveryNameRuleRepository<br/>
 * author         : Fiat_lux<br/>
 * date           : 2/15/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/15/24        Fiat_lux       최초 생성<br/>
 */
public interface DeliveryRuleNameRepository
        extends JpaRepository<DeliveryRuleName, String>, DeliveryRuleNameRepositoryCustom {
    /**
     * methodName : findDeliveryRuleNameById<br>
     * author : Fiat_lux<br>
     * description : deliveryRule read by DeliveryRuleName id
     *
     * @param id the delivery rule name id
     * @return DeliveryRuleNameDto Optional
     */
    Optional<DeliveryRuleNameDto> findDeliveryRuleNameById(String id);
}
