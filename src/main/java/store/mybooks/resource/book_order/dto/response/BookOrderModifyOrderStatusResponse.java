package store.mybooks.resource.book_order.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.book_order.dto.response<br>
 * fileName       : BookOrderModifyOrderStatusResponse<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
@Getter
@Setter
public class BookOrderModifyOrderStatusResponse {
    private Long id;
    private String statusId;
}
