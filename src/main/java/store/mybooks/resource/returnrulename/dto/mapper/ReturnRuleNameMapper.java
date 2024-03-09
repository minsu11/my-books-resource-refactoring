package store.mybooks.resource.returnrulename.dto.mapper;

import org.mapstruct.Mapper;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameCreateResponse;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;

/**
 * packageName    : store.mybooks.resource.return_name_rule.dto.mapper<br>
 * fileName       : ReturnNameRuleMapper<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    : 반품 규정 명 규칙 엔티티를 {@code mapStruct}를 사용하여 {@code ReturnNameRuleResponse}로 반환.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring")
public interface ReturnRuleNameMapper {
    ReturnRuleNameResponse mapToReturnRuleNameResponse(ReturnRuleName returnNameRule);

    ReturnRuleNameCreateResponse mapToReturnRuleNameCreateResponse(ReturnRuleName returnRuleName);
}
