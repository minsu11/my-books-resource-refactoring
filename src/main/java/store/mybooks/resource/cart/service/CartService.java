package store.mybooks.resource.cart.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.cart.dto.CartDto;
import store.mybooks.resource.cart.dto.CartMapper;
import store.mybooks.resource.cart.dto.CartRegisterRequest;
import store.mybooks.resource.cart.dto.CartResponse;
import store.mybooks.resource.cart.entity.Cart;
import store.mybooks.resource.cart.exception.CartAlreadyExistException;
import store.mybooks.resource.cart.exception.CartNotFoundException;
import store.mybooks.resource.cart.repository.CartRepository;
import store.mybooks.resource.user.entity.User;
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
    private final CartMapper cartMapper;


    public CartResponse registerCart(CartRegisterRequest cartRegisterRequest) {
        Optional<User> optionalUser = userRepository.findById(cartRegisterRequest.getUserId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("");
        } else if (cartRepository.existsCartByUserId(cartRegisterRequest.getUserId())) {
            throw new CartAlreadyExistException("");
        } else {
            User user = optionalUser.get();
            Cart cart = new Cart(user);
            Cart saveCart = cartRepository.save(cart);
            return cartMapper.mapToResponse(saveCart);
        }
    }


    public CartDto getCart(Long userId) {
        Optional<CartDto> optionalCart = cartRepository.findCartByUserId(userId);
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            throw new CartNotFoundException("");
        }
    }
}