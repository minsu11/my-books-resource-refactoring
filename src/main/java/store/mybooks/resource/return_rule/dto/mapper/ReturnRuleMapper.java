package store.mybooks.resource.return_rule.dto.mapper;

import org.mapstruct.Mapper;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleCreateResponse;
import store.mybooks.resource.return_rule.entity.ReturnRule;

/**
 * packageName    : store.mybooks.resource.return_rule.dto<br>
 * fileName       : ReturnRuleMapper<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring")
public interface ReturnRuleMapper {
    ReturnRuleCreateResponse mapToReturnRuleCreateResponse(ReturnRule returnRule);


}
