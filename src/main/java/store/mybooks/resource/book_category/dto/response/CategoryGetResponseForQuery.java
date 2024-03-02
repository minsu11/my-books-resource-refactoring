ㄹpackage store.mybooks.resource.book_category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book_category.dto.response <br/>
 * fileName       : CategoryGetResponseForQuery<br/>
 * author         : newjaehun <br/>
 * date           : 3/2/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/2/24        newjaehun       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
public class CategoryGetResponseForQuery {
    private Integer id;
    private String name;
    private Integer preParentId;
}
