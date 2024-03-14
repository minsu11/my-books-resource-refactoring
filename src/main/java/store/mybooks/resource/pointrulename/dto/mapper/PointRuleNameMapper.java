package store.mybooks.resource.pointrulename.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameCreateResponse;
import store.mybooks.resource.pointrulename.entity.PointRuleName;

/**
 * packageName    : store.mybooks.resource.point_rule_name.dto.mapper<br>
 * fileName       : PorintRuleNameMapper<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PointRuleNameMapper {

    PointRuleNameCreateResponse mapToPointRuleNameCreateResponse(PointRuleName pointRuleName);
}
