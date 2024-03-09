package store.mybooks.resource.pointhistory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.repository.PointHistoryRepository;

/**
 * packageName    : store.mybooks.resource.point_history.service<br>
 * fileName       : PointHistoryService<br>
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
@Transactional
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * methodName : getRemainingPoint<br>
     * author : minsu11<br>
     * description : 유저의 잔여 포인트 조회.
     * <br> *
     *
     * @param userId 잔여 포인트 조회 할 회원 {@code id}
     * @return point response
     */
    @Transactional(readOnly = true)
    public PointResponse getRemainingPoint(Long userId) {
        return pointHistoryRepository.getRemainingPoint(userId);
    }

    /**
     * methodName : getPointHistory<br>
     * author : minsu11<br>
     * description : 포인트 내역.
     * <br> *
     *
     * @param pageable 페이징
     * @param userId
     * @return page
     */
    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getPointHistory(Pageable pageable, Long userId) {
        return pointHistoryRepository.getPointHistoryByUserId(pageable, userId);
    }

}
