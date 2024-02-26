package store.mybooks.resource.cart.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.cart.dto.CartDto;
import store.mybooks.resource.cart.dto.CartMapper;
import store.mybooks.resource.cart.dto.CartRegisterRequest;
import store.mybooks.resource.cart.dto.CartResponse;
import store.mybooks.resource.cart.entity.Cart;
import store.mybooks.resource.cart.exception.CartAlreadyExistException;
import store.mybooks.resource.cart.exception.CartNotFoundException;
import store.mybooks.resource.cart.repository.CartRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

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
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;


    /**
     * Register cart cart response.
     *
     * @param cartRegisterRequest the cart register request
     * @return the cart response
     */
    @Transactional
    public CartResponse registerCart(CartRegisterRequest cartRegisterRequest) {
        User user = userRepository.findById(cartRegisterRequest.getUserId())
                .orElseThrow(() -> new UserNotExistException(cartRegisterRequest.getUserId()));

        if (cartRepository.existsCartByUserId(cartRegisterRequest.getUserId())) {
            throw new CartAlreadyExistException("카트가 이미 존재합니다");
        } else {
            Cart cart = new Cart(user);
            Cart saveCart = cartRepository.save(cart);
            return cartMapper.mapToResponse(saveCart);
        }
    }


    /**
     * Gets cart.
     *
     * @param userId the user id
     * @return the cart
     */
    public CartDto getCart(Long userId) {
        Optional<CartDto> optionalCart = cartRepository.findCartByUserId(userId);
        return optionalCart.orElseThrow(() -> new CartNotFoundException("카트 id를 찾을수 없습니다"));
    }
}