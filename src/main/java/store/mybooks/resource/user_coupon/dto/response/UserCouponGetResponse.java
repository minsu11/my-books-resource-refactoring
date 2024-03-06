package store.mybooks.resource.user_coupon.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user_coupon.dto.response
 * fileName       : CouponGetResponse
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class UserCouponGetResponse {
    private Long id;
    private String name;
    private String range;
    private String target;
    private Integer orderMin;
    private Integer discountRateOrCost;
    private Integer maxDiscountCost;
    private Boolean isRate;
    private LocalDate startDate;
    private LocalDate endDate;
}
