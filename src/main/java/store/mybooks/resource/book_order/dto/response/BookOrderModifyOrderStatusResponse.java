package store.mybooks.resource.book_order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
@AllArgsConstructor
public class BookOrderModifyOrderStatusResponse {
    private Long id;
    private String statusId;
}
