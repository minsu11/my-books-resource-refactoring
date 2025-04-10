package store.mybooks.resource.bookorder.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.bookorder.dto.request<br>
 * fileName       : BookInfoRequest<br>
 * author         : minsu11<br>
 * date           : 3/16/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/16/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class BookInfoRequest {
    @Positive
    private Long bookId;
    @PositiveOrZero
    private Integer saleCost;
    @PositiveOrZero
    private Integer bookCost;
    @PositiveOrZero
    private Integer amount;
    @Positive
    private Integer selectWrapId;
    @Positive
    private Long selectCouponId;
}
