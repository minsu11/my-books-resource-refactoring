package store.mybooks.resource.tag.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.tag.dto.response <br/>
 * fileName       : TagGetResponseForBookDetail<br/>
 * author         : newjaehun <br/>
 * date           : 3/2/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/2/24        newjaehun       최초 생성<br/>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagGetResponseForBookDetail {
    private Integer id;
    private String name;
}
