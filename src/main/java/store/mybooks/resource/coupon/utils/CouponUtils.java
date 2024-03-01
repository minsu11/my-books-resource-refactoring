package store.mybooks.resource.coupon.utils;

import java.time.LocalDate;
import store.mybooks.resource.coupon.exception.CouponDateIncorrectException;
import store.mybooks.resource.coupon.exception.OrderMinLessThanDiscountCostException;

/**
 * packageName    : store.mybooks.resource.error
 * fileName       : CouponUtils
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
public class CouponUtils {
    private CouponUtils() {}

    public static void validateCouponOrderMin(Integer orderMin, Integer discountCost) {
        if (orderMin <= discountCost) {
            throw new OrderMinLessThanDiscountCostException(orderMin, discountCost);
        }
    }

    public static void validateCouponDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new CouponDateIncorrectException(startDate, endDate);
        }
    }
}
