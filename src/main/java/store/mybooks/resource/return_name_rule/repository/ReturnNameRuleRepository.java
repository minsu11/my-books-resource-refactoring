package store.mybooks.resource.return_name_rule.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.return_name_rule.entity.ReturnNameRule;

/**
 * packageName    : store.mybooks.resource.return_name_rule.repository
 * fileName       : ReturnNameRuleRepository
 * author         : minsu11
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        minsu11       최초 생성
 */
public interface ReturnNameRuleRepository extends JpaRepository<ReturnNameRule, Integer> {
    Optional<ReturnNameRule> findByName(String name);
}
