package store.mybooks.resource.coupon.exception;

import javax.validation.ValidationException;

/**
 * packageName    : store.mybooks.resource.coupon.exception
 * fileName       : OrderMinLessThanDiscountCostException
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
public class OrderMinLessThanDiscountCostException extends ValidationException {
    public OrderMinLessThanDiscountCostException(Integer orderMin, Integer discountCost) {
        super(String.format("최소 주문 금액(%d) 은 정액할인액%d 보다 커야합니다.", orderMin, discountCost));
    }
}
