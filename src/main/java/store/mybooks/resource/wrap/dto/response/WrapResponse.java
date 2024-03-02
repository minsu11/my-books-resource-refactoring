package store.mybooks.resource.wrap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.wrap.dto.response<br>
 * fileName       : WrapResponse<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
public class WrapResponse {
    private String name;
    private Integer cost;
    private Boolean isAvailable;
}
