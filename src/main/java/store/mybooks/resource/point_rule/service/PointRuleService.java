package store.mybooks.resource.point_rule.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.point_rule.dto.mapper.PointRuleMapper;
import store.mybooks.resource.point_rule.dto.request.PointRuleCreateRequest;
import store.mybooks.resource.point_rule.dto.response.PointRuleCreateResponse;
import store.mybooks.resource.point_rule.dto.response.PointRuleResponse;
import store.mybooks.resource.point_rule.entity.PointRule;
import store.mybooks.resource.point_rule.exception.PointRuleNotExistException;
import store.mybooks.resource.point_rule.repository.PointRuleRepository;
import store.mybooks.resource.point_rule_name.entity.PointRuleName;
import store.mybooks.resource.point_rule_name.exception.PointRuleNameNotExistException;
import store.mybooks.resource.point_rule_name.repository.PointRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.point_rule.service<br>
 * fileName       : PointRuleService<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class PointRuleService {
    private final PointRuleRepository pointRuleRepository;
    private final PointRuleNameRepository pointRuleNameRepository;
    private final PointRuleMapper pointRuleMapper;


    /**
     * methodName : getPointRuleResponse<br>
     * author : minsu11<br>
     * description : {@code id}로 포인트 규정 조회.
     * <br> *
     *
     * @param id 조회할 포인트 규정 {@code id}
     * @return point rule response
     */
    public PointRuleResponse getPointRuleResponse(Integer id) {
        return pointRuleRepository.getPointRuleById(id)
                .orElseThrow(PointRuleNotExistException::new);
    }

    public List<PointRuleResponse> getPointRuleList() {
        return pointRuleRepository.getPointRuleList();
    }

    public PointRuleCreateResponse createPointRuleResponse(PointRuleCreateRequest request) {
        String name = request.getPointRuleName();
        PointRuleName pointRuleName = pointRuleNameRepository.findById(request.getPointRuleName())
                .orElseThrow(PointRuleNameNotExistException::new);
        PointRule pointRule = new PointRule(pointRuleName, request.getRate(), request.getCost());

        PointRule beforePointRule = pointRuleRepository.findPointRuleByPointRuleName(name)
                .orElse(null);
        if (Objects.nonNull(beforePointRule)) {
            beforePointRule.modifyPointRuleIsAvailable(false);
        }

        return pointRuleMapper.mapToPointRuleCreateResponse(pointRuleRepository.save(pointRule));
    }


}
