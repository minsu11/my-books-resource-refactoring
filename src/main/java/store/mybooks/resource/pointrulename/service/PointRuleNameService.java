package store.mybooks.resource.pointrulename.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.pointrulename.dto.mapper.PointRuleNameMapper;
import store.mybooks.resource.pointrulename.dto.request.PointRuleNameCreateRequest;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameCreateResponse;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.exception.PointRuleNameAlreadyExistException;
import store.mybooks.resource.pointrulename.exception.PointRuleNameNotExistException;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.point_rule_name.service<br>
 * fileName       : PointRuleNameService<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PointRuleNameService {
    private final PointRuleNameRepository pointRuleNameRepository;
    private final PointRuleNameMapper pointRuleNameMapper;

    /**
     * methodName : getPointRuleName<br>
     * author : minsu11<br>
     * description : 아이디에 맞는 포인트 명 조회.
     * <br> *
     *
     * @param id 조회할 아이디
     * @return point rule name response
     */
    @Transactional(readOnly = true)
    public PointRuleNameResponse getPointRuleName(String id) {
        return pointRuleNameRepository.getPointRuleNameById(id).orElseThrow(PointRuleNameNotExistException::new);
    }

    /**
     * methodName : getPointRuleNameList<br>
     * author : minsu11<br>
     * description : 포인트 명 전체 조회. 조회할 목록이 없다면 {@code empty list}반환
     * <br> *
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<PointRuleNameResponse> getPointRuleNameList() {
        return pointRuleNameRepository.getPointRuleNameList();
    }

    /**
     * methodName : createPointRuleName<br>
     * author : minsu11<br>
     * description : 포인트 명 생성.
     * <br> *
     *
     * @param request 생성할 포인트 명 <br>
     * @return point rule name create response
     */
    public PointRuleNameCreateResponse createPointRuleName(PointRuleNameCreateRequest request) {
        if (pointRuleNameRepository.existsById(request.getId())) {
            throw new PointRuleNameAlreadyExistException();
        }
        PointRuleName pointRuleName = new PointRuleName(request.getId());
        pointRuleNameRepository.save(pointRuleName);

        return pointRuleNameMapper.mapToPointRuleNameCreateResponse(pointRuleName);
    }

}
