package store.mybooks.resource.bookorder.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book_order.dto.request<br>
 * fileName       : BookOrderRegisterInvoiceRequest<br>
 * author         : minsu11<br>
 * date           : 3/4/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/4/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class BookOrderRegisterInvoiceRequest {
    @NotBlank
    @Positive
    private Long id;

    @NotBlank
    @Size(min = 10, max = 20)
    private String invoiceNumber;
}
