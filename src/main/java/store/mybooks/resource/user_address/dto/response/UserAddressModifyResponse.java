package store.mybooks.resource.user_address.dto.response;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user_address.dto.response
 * fileName       : UserAddressModifyResponse
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@Getter
@Setter
public class UserAddressModifyResponse {

    private String alias;

    private String roadName;

    private String detail;

    private Integer number;

    private String reference;


}
