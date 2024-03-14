package store.mybooks.resource.wrap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.wrap.dto.mapper.WrapMapper;
import store.mybooks.resource.wrap.dto.request.WrapCreateRequest;
import store.mybooks.resource.wrap.dto.request.WrapModifyRequest;
import store.mybooks.resource.wrap.dto.response.WrapCreateResponse;
import store.mybooks.resource.wrap.dto.response.WrapModifyResponse;
import store.mybooks.resource.wrap.dto.response.WrapPageResponse;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.entity.Wrap;
import store.mybooks.resource.wrap.exception.WrapAlreadyExistException;
import store.mybooks.resource.wrap.exception.WrapNotExistException;
import store.mybooks.resource.wrap.repository.WrapRepository;

/**
 * packageName    : store.mybooks.resource.wrap.service<br>
 * fileName       : WrapService<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WrapService {
    private final WrapRepository wrapRepository;

    private final WrapMapper wrapMapper;

    /**
     * methodName : getWrapById<br>
     * author : minsu11<br>
     * description : {@code id}로 조회된 포장지 조회.
     * <br> *
     *
     * @param id 조회할 포장지 {@code id}
     * @return wrap response
     * @throws WrapNotExistException {@code id}에 맞는 포장지가 없는 경우
     */
    @Transactional(readOnly = true)
    public WrapResponse getWrapById(Integer id) {
        return wrapRepository.findWrapResponseById(id)
                .orElseThrow(WrapNotExistException::new);

    }


    /**
     * methodName : getWrapResponseList<br>
     * author : minsu11<br>
     * description : 모든 포장지의 목록을 조회.ㄴ
     * <br> *
     *
     * @return {@code WrapResponse} 목록을 반환
     */
    @Transactional(readOnly = true)
    public List<WrapResponse> getWrapResponseList() {
        return wrapRepository.getWrapResponseList();

    }

    /**
     * methodName : getWrapPage<br>
     * author : minsu11<br>
     * description : 포장지 전체 목록에 대한 {@code pagination}.
     * <br> *
     *
     * @param pageable 페이징 처리
     * @return page
     */
    @Transactional(readOnly = true)
    public Page<WrapPageResponse> getWrapPage(Pageable pageable) {
        return wrapRepository.getPageBy(pageable);
    }

    /**
     * methodName : createWrap<br>
     * author : minsu11<br>
     * description : 포장지 등록. 포장지 등록에 실패 시 {@code WrapAlreadyExistException}을 던짐.
     * <br> *
     *
     * @param request 등록할 포장지
     * @return wrap create response
     * @throws WrapAlreadyExistException 포장지가 이미 존재 했을 경우
     */
    public WrapCreateResponse createWrap(WrapCreateRequest request) {
        if (Boolean.TRUE.equals(wrapRepository.existWrap(request.getName()))) {
            throw new WrapAlreadyExistException();
        }
        Wrap wrap = new Wrap(request.getName(), request.getCost());
        return wrapMapper.mapToWrapCreateResponse(wrapRepository.save(wrap));
    }

    /**
     * methodName : updateWrap<br>
     * author : minsu11<br>
     * description : 사용 중인 포장지의 정보 수정. {@code id} 조회 시 포장지가 없는 경우
     * {@code WrapNotExistException}을 던짐
     * <br>
     *
     * @param wrapModifyRequest 정보 수정할 포장지 정보
     * @param id                정보 수정할 포장지의 아이디
     * @return wrap modify response
     * @throws WrapNotExistException 조회한 포장지의 정보가 없는 경우
     */
    public WrapModifyResponse modifyWrap(WrapModifyRequest wrapModifyRequest, Integer id) {

        Wrap wrap = wrapRepository.findById(id).orElseThrow(WrapNotExistException::new);
        wrap.modifyWrap(wrapModifyRequest);

        return wrapMapper.mapToWrapModifyResponse(wrap);
    }

    /**
     * methodName : deleteWrap<br>
     * author : minsu11<br>
     * description : id 정보를 가진 포장지 삭제. 실제 삭제가 아닌 {@code isAvailable}을
     * {@code false}로 변경하여 사용 중이 아니면 삭제라고 판단
     * <br> *
     *
     * @param id 삭제할 포장지 아이디
     */
    public void deleteWrap(Integer id) {
        Wrap wrap = wrapRepository.findById(id).orElseThrow(WrapNotExistException::new);
        wrap.modifyIsAvailable(false);
    }

}
