package store.mybooks.resource.cart_item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.cart_item.dto <br/>
 * fileName       : CartDetail<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/9/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/9/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail {
    private Long bookId;
    private int cartDetailAmount;
    private String name;
    private String bookImage;
    private Integer cost;
    private Integer saleCost;
}
