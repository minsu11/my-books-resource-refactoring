package store.mybooks.resource.user_coupon.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForOrderQuerydsl;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForMyPageQuerydsl;

/**
 * packageName    : store.mybooks.resource.user_coupon.repository
 * fileName       : UserCouponRepositoryCustom
 * author         : damho-lee
 * date           : 3/5/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/5/24          damho-lee          최초 생성
 */
@NoRepositoryBean
public interface UserCouponRepositoryCustom {
    Page<UserCouponGetResponseForMyPageQuerydsl> getUserCoupons(Long userId, Pageable pageable);

    List<UserCouponGetResponseForOrderQuerydsl> getUsableUserCategoryCouponsByBookId(Long userId, Long bookId);

    List<UserCouponGetResponseForOrderQuerydsl> getUsableUserBookCouponsByBookId(Long userId, Long bookId);
}
