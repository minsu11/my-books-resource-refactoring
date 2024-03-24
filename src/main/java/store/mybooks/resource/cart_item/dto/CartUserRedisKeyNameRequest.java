package store.mybooks.resource.cart_item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.cart.dto <br/>
 * fileName       : CartUserRedisKeyNameRequest<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/10/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/10/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartUserRedisKeyNameRequest {
    private String cartKey;
}
