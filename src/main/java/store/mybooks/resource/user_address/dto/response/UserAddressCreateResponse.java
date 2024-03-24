package store.mybooks.resource.user_address.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user_address.dto.response<br>
 * fileName       : UserAddressCreateResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Getter
@AllArgsConstructor
public class UserAddressCreateResponse {

    private String alias;
    private String roadName;
    private String detail;
    private Integer number;
    private String reference;

}
