package store.mybooks.resource.point_history.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.point_history.dto.response.PointResponse;

/**
 * packageName    : store.mybooks.resource.point_history.repository<br>
 * fileName       : PointHistoryRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface PointHistoryRepositoryCustom {
    /**
     * methodName : getRemainingPoint<br>
     * author : minsu11<br>
     * description : 회원의 잔여 포인트 조회.
     * <br> *
     *
     * @param userId 잔여 포인트를 조회할 회원의 {@code id}
     * @return point response
     */
    PointResponse getRemainingPoint(Long userId);
}
