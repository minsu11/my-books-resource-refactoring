package store.mybooks.resource.book_order.dto.request;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private Long id;
}
