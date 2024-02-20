package store.mybooks.resource.user_address.dto.request;

import lombok.Getter;

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
public class UserAddressModifyRequest {

    private String alias;

    private String roadName;

    private String detail;

    private Integer number;

    private String reference;

}
