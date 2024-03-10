package store.mybooks.resource.coupon.exception;

/**
 * packageName    : store.mybooks.resource.coupon.exception
 * fileName       : CouponInCompatibleType
 * author         : damho-lee
 * date           : 3/2/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24          damho-lee          최초 생성
 */
public class CouponInCompatibleType extends RuntimeException {
    public CouponInCompatibleType() {
        super("형식에 맞지 않는 쿠폰입니다.");
    }
}
