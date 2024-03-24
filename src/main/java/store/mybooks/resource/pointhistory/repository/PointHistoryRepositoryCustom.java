package store.mybooks.resource.pointhistory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;

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

    /**
     * methodName : getPointHistoryByUserId <br>
     * author : damho-lee <br>
     * description : 포인트 내역 조회.<br>
     *
     * @param pageable 페이지 정보
     * @param userId   포인트 내역을 조회할 회원의 {@code id}
     * @return page
     */
    Page<PointHistoryResponse> getPointHistoryByUserId(Pageable pageable, Long userId);

    /**
     * methodName : isAlreadyReceivedSignUpPoint <br>
     * author : damho-lee <br>
     * description : 회원가입 포인트를 받은 적이 있는지 조회.<br>
     *
     * @param email 회원가입 포인트를 받았는지 조회할 회원의 {@code 이메일}
     * @return boolean
     */
    boolean isAlreadyReceivedSignUpPoint(String email);

    Integer getOrderUsedPoint(String orderNumber);
}
