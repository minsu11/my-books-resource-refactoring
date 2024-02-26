package store.mybooks.resource.wrap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.wrap.dto.response<br>
 * fileName       : WrapCreateResponse<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    : 포장지가 정상 등록이 되면 반환되는 DTO
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WrapCreateResponse {
    private String wrapName;
    private Integer wrapCost;
}
