package store.mybooks.resource.cart_item.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    /**
     * methodName : moveDataMysqlToRedis<br>
     * author : Fiat_lux<br>
     * description : post 요청으로 key 값을 가져와서 mysql에 있는 장바구니 데이터 redis로 이동 응답 <br>
     *
     * @param userId                      : user id
     * @param cartUserRedisKeyNameRequest : user의 redis의 키값
     * @return cartDetail list<br>
     */
    @PostMapping("/get/items")
    public ResponseEntity<List<CartDetail>> moveDataMysqlToRedis(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @RequestBody CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest) {
        List<CartDetail> cartDetailList = cartItemService.registerMysqlToRedis(userId, cartUserRedisKeyNameRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartDetailList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> cartItemAllDelete(@RequestHeader(name = HeaderProperties.USER_ID) Long userId) {
        cartItemService.deleteAllItem(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> cartItemDelete(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @PathVariable("id") Long id
    ) {
        cartItemService.deleteItem(userId, id);

        return ResponseEntity.noContent().build();
    }
}
