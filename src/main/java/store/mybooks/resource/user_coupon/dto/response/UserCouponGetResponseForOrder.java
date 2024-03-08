package store.mybooks.resource.user_coupon.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user_coupon.dto.response
 * fileName       : UserCouponGetResponseForOrder
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
public class UserCouponGetResponseForOrder {
    private Long userCouponId;
    private String name;
    private Integer orderMin;
    private Integer discountRateOrCost;
    private Integer maxDiscountCost;
    private Boolean isRate;
    private LocalDate startDate;
    private LocalDate endDate;
}
