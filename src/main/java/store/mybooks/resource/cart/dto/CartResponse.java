package store.mybooks.resource.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.cart.dto
 * fileName       : CartResponse
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class CartResponse {
    private Long id;
    private Long userId;
}