package store.mybooks.resource.user_address.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user_address.dto.request
 * fileName       : UserAddressModifyRequest
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressModifyRequest {

    private String alias;

    private String detail;


}
