package store.mybooks.resource.book.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookPublicationDateResponse<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/21/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/21/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
public class BookPublicationDateResponse {
    private Long id;
    private String image;
    private String name;
    private Long reviewCount;
    private Integer cost;
    private Integer saleCost;
    private Double rate;
    private LocalDate publicationDate;
}
