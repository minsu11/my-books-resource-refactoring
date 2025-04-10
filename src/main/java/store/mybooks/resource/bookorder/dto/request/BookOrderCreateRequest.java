package store.mybooks.resource.bookorder.dto.request;

import java.util.List;
import jakarta.validation.constraints.*;
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
    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @Email
    @NotBlank
    @Size(max = 30)
    private String email;

    @NotBlank
    @Size(min = 10, max = 13)
    private String phone;

    @NotNull
    private List<BookInfoRequest> bookInfoList;

    @NotNull
    private BookOrderInfoRequest orderInfo;

    @NotBlank
    @Size(max = 20)
    private String orderNumber;

    @NotNull
    @PositiveOrZero
    private Integer pointCost;

    @NotNull
    @PositiveOrZero
    private Integer couponCost;

    @NotNull
    @Positive
    private Integer totalCost;

    @PositiveOrZero
    private Integer wrapCost;

    @Size(max = 6)
    private String orderCode;
}
