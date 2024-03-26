package store.mybooks.resource.user_address.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.user_address.dto.response<br>
 * fileName       : UserAddressModifyResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@Getter
@AllArgsConstructor
public class UserAddressModifyResponse {

    private String alias;

    private String detail;

    private String reference;


}
