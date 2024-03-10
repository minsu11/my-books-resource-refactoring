package store.mybooks.resource.delivery_rule_name.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.repository
 * fileName       : DeliveryNameRuleRepository
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
public interface DeliveryRuleNameRepository extends JpaRepository<DeliveryRuleName, String> {
    /**
     * Find delivery rule name by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<DeliveryRuleNameDto> findDeliveryRuleNameById(String id);
}
