package store.mybooks.resource.user_coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForQuerydsl;

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
    Page<UserCouponGetResponseForQuerydsl> getUserCoupons(Long userId, Pageable pageable);
}
