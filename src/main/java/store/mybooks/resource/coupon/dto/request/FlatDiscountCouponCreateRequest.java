package store.mybooks.resource.coupon.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.coupon.dto.request
 * fileName       : FlatDiscountCouponCreateRequest
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
public class FlatDiscountCouponCreateRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @PositiveOrZero
    private Integer orderMin;

    @Positive
    private Integer discountCost;

    @FutureOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;
}
