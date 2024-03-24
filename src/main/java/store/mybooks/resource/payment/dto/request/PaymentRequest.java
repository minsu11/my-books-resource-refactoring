package store.mybooks.resource.payment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.payment.dto.request<br>
 * fileName       : PaymentRequest<br>
 * author         : minsu11<br>
 * date           : 3/23/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/23/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PaymentRequest {
    @NotBlank
    @Size(min = 0, max = 20)
    private String orderNumber;

}
