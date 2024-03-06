package store.mybooks.resource.user_coupon.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user_coupon.dto.response
 * fileName       : UserCouponGetResponseForQuerydsl
 * author         : damho-lee
 * date           : 3/5/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/5/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class UserCouponGetResponseForMyPageQuerydsl {
    private Long id;
    private String name;
    private Integer orderMin;
    private Integer discountCost;
    private Integer maxDiscountCost;
    private Integer discountRate;
    private Boolean isRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String bookName;
    private String categoryName;
}
