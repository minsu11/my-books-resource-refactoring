package store.mybooks.resource.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookGetResponseForCoupon<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
public class BookGetResponseForCoupon {
    private Long id;
    private String name;
}
