package store.mybooks.resource.delivery_name_rule.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;

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
public interface DeliveryNameRuleRepository extends JpaRepository<DeliveryNameRule, Integer> {
    Optional<DeliveryNameRuleDto> findDeliveryNameRuleById(Integer deliveryNameRuleId);
}
