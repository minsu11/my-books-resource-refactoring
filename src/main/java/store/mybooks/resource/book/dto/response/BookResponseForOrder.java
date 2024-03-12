package store.mybooks.resource.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookResponseForOrder<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
public class BookResponseForOrder {
    private String name;
    //    private String bookImage;
    private Integer saleCost;
    private Integer originalCost;
    private Integer disCountRate;
    private Boolean isPacking;
}
