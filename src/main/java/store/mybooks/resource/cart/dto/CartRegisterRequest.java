package store.mybooks.resource.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.cart.dto
 * fileName       : CartRegisterRequest
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class CartRegisterRequest {
    private Long userId;
}
