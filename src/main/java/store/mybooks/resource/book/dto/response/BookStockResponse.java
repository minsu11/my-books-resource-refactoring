package store.mybooks.resource.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book.dto.response<br>
 * fileName       : BookStockResponse<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class BookStockResponse {
    private Long id;
    private Integer stock;
}
