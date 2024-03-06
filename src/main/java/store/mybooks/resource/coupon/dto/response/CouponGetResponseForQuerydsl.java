package store.mybooks.resource.coupon.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.coupon.dto.response
 * fileName       : CouponGetResponseForQuerydsl
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
public class CouponGetResponseForQuerydsl {
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
