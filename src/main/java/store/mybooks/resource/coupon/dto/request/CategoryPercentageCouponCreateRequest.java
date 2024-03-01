package store.mybooks.resource.coupon.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.coupon.dto.request
 * fileName       : CategoryPercentageCoupon
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
@Getter
@NoArgsConstructor
public class CategoryPercentageCouponCreateRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Positive
    private Integer categoryId;

    @PositiveOrZero
    private Integer orderMin;

    @Positive
    private Integer maxDiscountCost;

    @Min(1)
    @Max(99)
    private Integer discountRate;

    @FutureOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;
}
