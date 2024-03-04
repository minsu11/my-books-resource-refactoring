package store.mybooks.resource.cart_item.service;

import org.springframework.stereotype.Service;
import store.mybooks.resource.cart_item.repository.CartItemRepository;

/**
 * packageName    : store.mybooks.resource.cart_item.service
 * fileName       : CartItemService
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }




}
