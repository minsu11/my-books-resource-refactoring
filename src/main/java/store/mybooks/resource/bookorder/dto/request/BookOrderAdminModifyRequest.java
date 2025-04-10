package store.mybooks.resource.bookorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book_order.dto.request<br>
 * fileName       : BookOrderModifyOrderStatusReqeust<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class BookOrderAdminModifyRequest {
    @NotNull
    @Positive
    private Long id;

    @NotBlank(message = "송장 번호 에러")
    @Size(min = 8, max = 20, message = "송장번호 사이즈 에러")
    private String invoiceNumber;
}
