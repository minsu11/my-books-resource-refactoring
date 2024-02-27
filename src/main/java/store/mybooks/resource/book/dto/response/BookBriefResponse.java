package store.mybooks.resource.book.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookBriefResponse<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
@Getter
@Setter
public class BookBriefResponse {
    private Long id;

    private String bookImage;

    private String name;

    private Double rate;

    private Integer saleCost;
}
