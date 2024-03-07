package store.mybooks.resource.point_rule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.point_rule.dto.response.PointRuleResponse;
import store.mybooks.resource.point_rule.exception.PointRuleNotExistException;
import store.mybooks.resource.point_rule.repository.PointRuleRepository;

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


}
