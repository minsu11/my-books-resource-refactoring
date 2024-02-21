package store.mybooks.resource.return_rule_name.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.mybooks.resource.return_rule_name.entity.ReturnRuleName;

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
@Repository
public interface ReturnRuleNameRepository extends JpaRepository<ReturnRuleName, String>, ReturnRuleNameRepositoryCustom {
}
