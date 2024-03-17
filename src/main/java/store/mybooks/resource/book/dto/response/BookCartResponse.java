package store.mybooks.resource.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookCartResponse<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/5/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/5/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCartResponse {
    private Long id;
    private String name;
    private String bookImage;
    private Integer cost;
    private Integer saleCost;
    private Integer stock;
    private String sellingStatus;
}
