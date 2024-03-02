package store.mybooks.resource.coupon.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.coupon.dto.request
 * fileName       : BookPercentageCouponCreateRequest
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
public class BookPercentageCouponCreateRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Positive
    private Long bookId;

    @NotNull
    @PositiveOrZero
    private Integer orderMin;

    @NotNull
    @Positive
    private Integer maxDiscountCost;

    @NotNull
    @Min(1)
    @Max(99)
    private Integer discountRate;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;
}
