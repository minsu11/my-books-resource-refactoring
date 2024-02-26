package store.mybooks.resource.wrap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
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
public class WrapService {
    private final WrapRepository wrapRepository;

    /**
     * methodName : getWrapById<br>
     * author : minsu11<br>
     * description : {@code id}로 조회된 포장지 조회
     * <br> *
     *
     * @param id 조회할 포장지 {@code id}
     * @return wrap response
     * @throws WrapNotExistException {@code id}에 맞는 포장지가 없는 경우
     */
    public WrapResponse getWrapById(Integer id) {
        return wrapRepository.findWrapResponseById(id)
                .orElseThrow(WrapNotExistException::new);

    }


    /**
     * methodName : getWrapResponseList<br>
     * author : minsu11<br>
     * description : 모든 포장지의 목록을 조회
     * <br> *
     *
     * @return {@code WrapResponse} 목록을 반환
     */
    public List<WrapResponse> getWrapResponseList() {
        return wrapRepository.getWrapResponseList();

    }


}
