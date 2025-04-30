package store.mybooks.resource.payment.dto.request;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.payment.dto.request<br>
 * fileName       : PayCreateRequest<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PayCreateRequest {
    @Size(max = 20)
    @NotBlank
    private String orderNumber;
    @NotBlank
    @Size(max = 50)
    private String paymentKey;
    @Size(max = 20)
    @NotBlank
    private String status;
    @NotBlank
    private String requestedAt;

    @Positive
    @NotNull
    @Max(Integer.MAX_VALUE - 1)
    private Integer totalAmount;

    @NotBlank
    private String method;
}
