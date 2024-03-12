package store.mybooks.resource.returnrulename.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;

/**
 * packageName    : store.mybooks.resource.return_name_rule.repository
 * fileName       : ReturnNameRuleRepository
 * author         : minsu11
 * date           : 2/20/24
 * description    : 반품 규정 명에 대한 query문을 선언할 수 있는 {@code repository}
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        minsu11       최초 생성
 */
public interface ReturnRuleNameRepository extends JpaRepository<ReturnRuleName, String>, ReturnRuleNameRepositoryCustom {
}
