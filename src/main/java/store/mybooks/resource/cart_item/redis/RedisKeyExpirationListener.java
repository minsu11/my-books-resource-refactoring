package store.mybooks.resource.cart_item.redis;


import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import store.mybooks.front.cart.domain.CartDetail;
import store.mybooks.resource.cart_item.service.CartItemService;

/**
 * packageName    : store.mybooks.resource.cart_item.redis <br/>
 * fileName       : RedisKeyExpirationListener<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/20/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/20/24        Fiat_lux       최초 생성<br/>
 */
@Component
@RequiredArgsConstructor
public class RedisKeyExpirationListener implements MessageListener {
    private final RedisTemplate<String, CartDetail> redisTemplate;
    private final CartItemService cartItemService;
    private static final String EXPIRED_KEY_CART = "EXPIRED_CART";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        if (expiredKey.startsWith(EXPIRED_KEY_CART)) {
            String cartKey = parsingCartKeyFromExpiredKey(expiredKey);
            String userEmail = parsingUserEmailFromCartKey(cartKey);
            List<CartDetail> cartDetailList = redisTemplate.opsForList().range(cartKey, 0, -1);
            cartItemService.registerRedisToMysql(userEmail, Objects.requireNonNull(cartDetailList));
        }
    }

    private String parsingCartKeyFromExpiredKey(String expiredKey) {
        return expiredKey.split(" ")[1];
    }

    private String parsingUserEmailFromCartKey(String cartKey) {
        return cartKey.split(":")[1];
    }
}
