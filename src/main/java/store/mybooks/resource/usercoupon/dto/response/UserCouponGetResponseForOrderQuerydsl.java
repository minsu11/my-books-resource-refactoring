package store.mybooks.resource.usercoupon.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user_coupon.dto.request
 * fileName       : UserCategoryCouponGetResponse
 * author         : damho-lee
 * date           : 3/6/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/6/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class UserCouponGetResponseForOrderQuerydsl {
    private Long userCouponId;
    private String name;
    private Integer orderMin;
    private Integer discountCost;
    private Integer maxDiscountCost;
    private Integer discountRate;
    private boolean isRate;
    private LocalDate startDate;
    private LocalDate endDate;
}
