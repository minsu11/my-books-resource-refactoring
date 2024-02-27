package store.mybooks.resource.return_rule.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleCreateResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleModifyResponse;
import store.mybooks.resource.return_rule.entity.ReturnRule;

/**
 * packageName    : store.mybooks.resource.return_rule.dto<br>
 * fileName       : ReturnRuleMapper<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    : 반품 규정 {@code entity}를 {@code mapStruct} 사용해서 DTO로 변환 해주는 {@code interface}.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReturnRuleMapper {
    @Mapping(source = "returnRuleName.id", target = "returnRuleName")
    ReturnRuleCreateResponse mapToReturnRuleCreateResponse(ReturnRule returnRule);

    @Mapping(source = "returnRuleName.id", target = "returnRuleNameId")
    ReturnRuleModifyResponse mapToReturnRuleModifyResponse(ReturnRule returnRule);

}
