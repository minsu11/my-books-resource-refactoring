package store.mybooks.resource.coupon.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.coupon.dto.request
 * fileName       : CouponCreateRequest
 * author         : damho-lee
 * date           : 3/2/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24          damho-lee          최초 생성
 */
@Getter
@NoArgsConstructor
public class CouponCreateRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Positive
    private Long bookId;

    @Positive
    private Integer categoryId;

    @NotNull
    @PositiveOrZero
    private Integer orderMin;

    @Positive
    private Integer discountCost;

    @Positive
    private Integer maxDiscountCost;

    @Positive
    private Integer discountRate;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;
}
