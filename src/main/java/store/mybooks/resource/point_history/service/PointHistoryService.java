package store.mybooks.resource.point_history.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.point_history.dto.response.PointResponse;
import store.mybooks.resource.point_history.repository.PointHistoryRepository;

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
     * description : 유저의 잔여 포인트 조회
     * <br> *
     *
     * @param userId 잔여 포인트 조회 할 회원 {@code id}
     * @return point response
     */
    @Transactional(readOnly = true)
    public PointResponse getRemainingPoint(Long userId) {
        // 포인트에서 유저인지 아닌지 확인을 해야하나?
        return pointHistoryRepository.getRemainingPoint(userId);
    }

}
