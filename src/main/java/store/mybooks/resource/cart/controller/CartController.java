package store.mybooks.resource.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.cart.dto.CartDto;
import store.mybooks.resource.cart.dto.CartRegisterRequest;
import store.mybooks.resource.cart.dto.CartResponse;
import store.mybooks.resource.cart.service.CartService;

/**
 * packageName    : store.mybooks.resource.cart.controller
 * fileName       : CartController
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> registerCart(@RequestBody CartRegisterRequest cartRegisterRequest) {
        CartResponse cartResponse = cartService.registerCart(cartRegisterRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartResponse);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long cartId) {
        CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartDto);
    }

}
