package store.mybooks.resource.user_address.dto.request;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user_address.dto.request<br>
 * fileName       : UserAddressCreateRequest<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressCreateRequest {

    @NotBlank
    private String alias;

    @NotBlank
    private String roadName;

    @NotBlank
    private String detail;


    @NotNull
    private Integer number;

    @NotBlank
    private String reference;
}
