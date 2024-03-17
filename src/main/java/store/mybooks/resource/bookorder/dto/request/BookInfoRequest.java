package store.mybooks.resource.bookorder.dto.request;

import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@ToString
public class BookInfoRequest {
    @Positive
    private Long bookId;
    @Positive
    private Integer saleCost;
    @Positive
    private Integer bookCost;
    @Positive
    private Integer amount;
    @Positive
    private Integer selectWrapId;
    @Positive
    private Long selectCouponId;
}
