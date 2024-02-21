package store.mybooks.resource.user_address.dto.request;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user_address.dto.request
 * fileName       : UserAddressCreateRequest
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@Getter
public class UserAddressCreateRequest {

    private String alias;

    private String roadName;

    private String detail;

    private Integer number;

    private String reference;
}
