package store.mybooks.resource.coupon.exception;

/**
 * packageName    : store.mybooks.resource.coupon.exception
 * fileName       : CouponCannotDeleteException
 * author         : damho-lee
 * date           : 3/5/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/5/24          damho-lee          최초 생성
 */
public class CouponCannotDeleteException extends RuntimeException {
    public CouponCannotDeleteException(Long id) {
        super(String.format("CouponId : %d 인 쿠폰은 이미 회원들에게 제공돼 삭제할 수 없습니다.", id));
    }
}
