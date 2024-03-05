package store.mybooks.resource.user_coupon.exception;

/**
 * packageName    : store.mybooks.resource.user_coupon.exception
 * fileName       : UserCouponNotUsedException
 * author         : damho-lee
 * date           : 3/5/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/5/24          damho-lee          최초 생성
 */
public class UserCouponNotUsedException extends RuntimeException {
    public UserCouponNotUsedException(Long id) {
        super(String.format("UserCoupon : %d 는 사용된 쿠폰이 아닙니다.", id));
    }
}
