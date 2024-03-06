package store.mybooks.resource.coupon.exception;

/**
 * packageName    : store.mybooks.resource.coupon.exception
 * fileName       : CouponNotExistsException
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
public class CouponNotExistsException extends RuntimeException {
    public CouponNotExistsException(long couponId) {
        super(String.format("CouponId : %d 인 쿠폰을 찾을 수 없습니다.", couponId));
    }
}
