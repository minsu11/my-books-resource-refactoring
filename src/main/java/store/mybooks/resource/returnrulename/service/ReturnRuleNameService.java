package store.mybooks.resource.returnrulename.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.returnrulename.dto.mapper.ReturnRuleNameMapper;
import store.mybooks.resource.returnrulename.dto.request.ReturnRuleNameCreateRequest;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameCreateResponse;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.entity.ReturnRuleName;
import store.mybooks.resource.returnrulename.exception.ReturnRuleNameAlreadyExistException;
import store.mybooks.resource.returnrulename.exception.ReturnRuleNameNotExistException;
import store.mybooks.resource.returnrulename.repository.ReturnRuleNameRepository;

/**
 * packageName    : store.mybooks.resource.return_name_rule.service
 * fileName       : ReturnNameRuleService
 * author         : minsu11
 * date           : 2/20/24
 * description    : 반품 규정 명 규칙에 대한 crud
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        minsu11       최초 생성
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReturnRuleNameService {
    private final ReturnRuleNameRepository returnRuleNameRepository;
    private final ReturnRuleNameMapper returnRuleNameMapper;

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : 요청된 name 값과 동일한 반품 규정 명 조회. <br>
     *
     * @param id 조회할 id
     * @return return name rule response
     * @throws ReturnRuleNameNotExistException name 값과 동일한 데이터를 찾지 못한 경우
     */
    @Transactional(readOnly = true)
    public ReturnRuleNameResponse getReturnRuleName(String id) {
        return returnRuleNameRepository.findReturnRuleNameById(id)
                .orElseThrow(ReturnRuleNameNotExistException::new);
    }

    /**
     * methodName : getReturnNameRule<br>
     * author : minsu11<br>
     * description : 반품 규정 명 테이블의 모든 데이터를 조회.<br>
     *
     * @return the return rule name list
     */
    @Transactional(readOnly = true)
    public List<ReturnRuleNameResponse> getReturnRuleNameList() {
        return returnRuleNameRepository.getReturnRuleNameList();
    }


    /**
     * methodName : createReturnRuleName<br>
     * author : minsu11<br>
     * description : 요청 들어온 반품 규정 명을 DB에 저장. <br>
     * 등록 시 DB에 존재 한다면 ReturnRuleNameAlreadyExistException 던짐.
     *
     * @param request the request
     * @return the return rule name create response
     * @throws ReturnRuleNameAlreadyExistException id의 값이 이미 존재하는 경우
     */
    public ReturnRuleNameCreateResponse createReturnRuleName(ReturnRuleNameCreateRequest request) {
        if (returnRuleNameRepository.existsById(request.getId())) {
            throw new ReturnRuleNameAlreadyExistException();
        }

        ReturnRuleName returnRuleName = new ReturnRuleName(request.getId(), LocalDate.now());
        returnRuleNameRepository.save(returnRuleName);
        return returnRuleNameMapper.mapToReturnRuleNameCreateResponse(returnRuleName);
    }
}
