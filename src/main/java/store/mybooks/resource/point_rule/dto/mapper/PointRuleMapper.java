package store.mybooks.resource.point_rule.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.point_rule.dto.response.PointRuleCreateResponse;
import store.mybooks.resource.point_rule.dto.response.PointRuleModifyResponse;
import store.mybooks.resource.point_rule.entity.PointRule;

/**
 * packageName    : store.mybooks.resource.point_rule.dto.mapper<br>
 * fileName       : PointRuleMapper<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PointRuleMapper {
    @Mapping(source = "pointRuleName.id", target = "pointRuleName")
    PointRuleCreateResponse mapToPointRuleCreateResponse(PointRule pointRule);

    @Mapping(source = "pointRuleName.id", target = "pointRuleName")
    PointRuleModifyResponse mapToPointRuleModifyResponse(PointRule pointRule);
}
