package store.mybooks.resource.bookorder.dto.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class BookOrderCreateRequest {
    private List<BookInfoRequest> bookInfoList;

    @NotBlank
    @Size(min = 1, max = 50)
    private String deliveryName;

    @FutureOrPresent
    private LocalDate deliveryDate;
    @Min(0)
    private Integer point;

    @NotBlank
    @Size(min = 3, max = 20)
    private String recipientName;

    @NotBlank
    @Size(max = 100)
    private String recipientAddress;

    @NotBlank
    @Size(max = 13)
    private String recipientPhoneNumber;

    private String receiverMessage;
    @NotBlank
    @Size(max = 20)
    private String orderNumber;


}
