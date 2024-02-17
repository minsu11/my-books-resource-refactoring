package store.mybooks.resource.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRegisterRequest {
    Long userId;
}
