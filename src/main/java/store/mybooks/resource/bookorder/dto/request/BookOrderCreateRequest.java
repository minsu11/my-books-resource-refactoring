package store.mybooks.resource.bookorder.dto.request;

import java.util.List;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.book_order.dto.request
 * fileName       : BookOrderRequest
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Getter
@NoArgsConstructor
@Setter
public class BookOrderCreateRequest {
    private String name;
    private String email;
    private String phone;

    @NotNull
    private List<BookInfoRequest> bookInfoList;

    private BookOrderInfoRequest orderInfo;

    @NotBlank
    @Size(max = 20)
    private String orderNumber;

    @PositiveOrZero
    private Integer pointCost;

    @PositiveOrZero
    private Integer couponCost;

    @Positive
    private Integer totalCost;
    @Positive
    private Integer wrapCost;


}
