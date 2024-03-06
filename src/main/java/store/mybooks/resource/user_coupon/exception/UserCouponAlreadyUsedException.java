package store.mybooks.resource.user_coupon.exception;

/**
 * packageName    : store.mybooks.resource.user_coupon.exception
 * fileName       : UserCouponAlreadyUsed
 * author         : damho-lee
 * date           : 3/5/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/5/24          damho-lee          최초 생성
 */
public class UserCouponAlreadyUsedException extends RuntimeException {
    public UserCouponAlreadyUsedException(Long id) {
        super(String.format("UserCoupon : %d 는 이미 사용된 쿠폰입니다.", id));
    }
}
