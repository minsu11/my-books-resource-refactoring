package store.mybooks.resource.bookorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    @Size(min = 10, max = 20)
    private String invoiceNumber;
}
