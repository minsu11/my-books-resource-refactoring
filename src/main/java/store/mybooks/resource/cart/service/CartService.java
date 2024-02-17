package store.mybooks.resource.cart.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.cart.dto.CartRegisterRequest;
import store.mybooks.resource.cart.entity.Cart;
import store.mybooks.resource.cart.repository.CartRepository;
import store.mybooks.resource.user.entity.UserRepository;

/**
 * packageName    : store.mybooks.resource.cart.service
 * fileName       : CartService
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;


    public void createCart(CartRegisterRequest cartRegisterRequest) {
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(cartRegisterRequest.getUserId());
        if (optionalCart.isPresent()) {

        } else {

            return;
        }

    }

    public Cart getCart(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(userId);

        return optionalCart.orElseThrow(null);
    }


}
