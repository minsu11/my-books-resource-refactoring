package store.mybooks.resource.delivery_rule.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;

/**
 * packageName    : store.mybooks.resource.delivery_rule.repository
 * fileName       : DeliveryRuleRepository
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
public interface DeliveryRuleRepository extends JpaRepository<DeliveryRule, Integer> {
    /**
     * Find delivery rule by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<DeliveryRuleDto> findDeliveryRuleById(Integer id);
}
