package store.mybooks.resource.cart_item.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.front.cart.domain.CartDetail;
import store.mybooks.resource.cart_item.dto.CartUserRedisKeyNameRequest;
import store.mybooks.resource.cart_item.service.CartItemService;
import store.mybooks.resource.config.HeaderProperties;

/**
 * packageName    : store.mybooks.resource.cart_item.controller <br/>
 * fileName       : CartItemController<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/8/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/8/24        Fiat_lux       최초 생성<br/>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartItemController {

    private final CartItemService cartItemService;


    @PostMapping("/get/items")
    public ResponseEntity<List<CartDetail>> moveDataMysqlToRedis(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @RequestBody CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest) {
        List<CartDetail> cartDetailList = cartItemService.registerMysqlToRedis(userId, cartUserRedisKeyNameRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartDetailList);
    }
}
