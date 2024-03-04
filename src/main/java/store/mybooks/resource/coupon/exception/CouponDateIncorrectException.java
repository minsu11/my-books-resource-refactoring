package store.mybooks.resource.coupon.exception;

import java.time.LocalDate;
import javax.validation.ValidationException;

/**
 * packageName    : store.mybooks.resource.coupon.exception
 * fileName       : CouponDateIncorrectException
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
public class CouponDateIncorrectException extends ValidationException {
    public CouponDateIncorrectException(LocalDate startDate, LocalDate endDate) {
        super(String.format("쿠폰 시작일(%s) 이 쿠폰 종료일(%s) 보다 빨라야합니다", startDate.toString(), endDate.toString()));
    }
}
