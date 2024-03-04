package store.mybooks.resource.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.coupon.entity.Coupon;

/**
 * packageName    : store.mybooks.resource.coupon.entity.repository
 * fileName       : CouponRepository
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
