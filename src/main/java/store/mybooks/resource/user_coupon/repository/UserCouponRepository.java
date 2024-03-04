package store.mybooks.resource.user_coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user_coupon.entity.UserCoupon;

/**
 * packageName    : store.mybooks.resource.user_coupon.repository
 * fileName       : UserCouponRepository
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
