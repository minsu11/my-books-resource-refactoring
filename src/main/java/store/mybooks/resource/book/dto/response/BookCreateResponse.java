package store.mybooks.resource.book.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookCreateResponse<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
@Getter
@Setter
public class BookCreateResponse {
    private Long id;
    private String name;
}
