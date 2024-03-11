package store.mybooks.resource.usercoupon.exception;

/**
 * packageName    : store.mybooks.resource.user_coupon.exception
 * fileName       : UserCouponNotExistsException
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
public class UserCouponNotExistsException extends RuntimeException {
    public UserCouponNotExistsException(Long id) {
        super(String.format("UserCouponId : %d 인 회원쿠폰이 존재하지 않습니다.", id));
    }
}
