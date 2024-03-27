package store.mybooks.resource.cart_item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.front.cart.domain.CartDetail;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.cart.entity.Cart;
import store.mybooks.resource.cart.repository.CartRepository;
import store.mybooks.resource.cart_item.dto.CartUserRedisKeyNameRequest;
import store.mybooks.resource.cart_item.entity.CartItem;
import store.mybooks.resource.cart_item.repository.CartItemRepository;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.service.ImageService;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

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
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final RedisTemplate<String, CartDetail> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ImageService imageService;
    private static final String EXPIRED_KEY = "EXPIRED_CART";

    public void registerRedisToMysql(String userEmail, List<CartDetail> cartDetailList) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotExistException("해당하는 이메일의 유저가 없습니다."));
        Cart userCart =
                cartRepository.findCartByUserId(user.getId()).orElseGet(() -> cartRepository.save(new Cart(user)));
        cartItemRepository.deleteAllByCart_Id(userCart.getId());

        for (CartDetail cartDetail : cartDetailList) {
            Book book = bookRepository.findById(cartDetail.getBookId()).orElseThrow();
            CartItem cartItem = new CartItem(cartDetail.getCartDetailAmount(), userCart, book);
            cartItemRepository.save(cartItem);
        }
    }

    public List<CartDetail> registerMysqlToRedis(Long userId, CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest) {
        log.debug("registerMysqlToRedis 메서드에 들어왔습니다");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));
        log.debug("user가 있나 봅니다.");
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());
        if (optionalCart.isEmpty()) {
            log.debug("cart가 비어있습니다.");
            return new ArrayList<>();
        }
        log.debug("cart가 있나봅니다.");
        Cart userCart = optionalCart.get();
        List<CartItem> cartItemList = cartItemRepository.findCartItemsByCart_Id(userCart.getId());
        log.debug("cartItemList 입니다:  {}", cartItemList);
        if (cartItemList.isEmpty()) {
            log.debug("cartItemList 가 비어있습니다.");
            return new ArrayList<>();
        }
        log.debug("cartItemList 입니다 : {}", cartItemList);
        redisTemplate.delete(cartUserRedisKeyNameRequest.getCartKey());
        List<CartDetail> cartDetailList = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {
            Image thumbNailImage = imageService.getThumbNailImage(cartItem.getBook().getId());
            CartDetail cartDetail = new CartDetail(
                    cartItem.getBook().getId(),
                    cartItem.getAmount(),
                    cartItem.getBook().getName(),
                    imageService.getObject(thumbNailImage.getId()).getFilePathName(),
                    cartItem.getBook().getOriginalCost(),
                    cartItem.getBook().getSaleCost(),
                    cartItem.getBook().getStock(),
                    cartItem.getBook().getBookStatus().getId());
            cartDetailList.add(cartDetail);
            redisTemplate.opsForList().rightPush(
                    cartUserRedisKeyNameRequest.getCartKey(), cartDetail
            );
        }
        log.debug("최종 입니다");
        log.debug("cartDetailList 입니다 {}", cartDetailList);
        stringRedisTemplate.expire(getExpiredKey(cartUserRedisKeyNameRequest.getCartKey()), 179, TimeUnit.MINUTES);
        redisTemplate.expire(cartUserRedisKeyNameRequest.getCartKey(), 180, TimeUnit.MINUTES);


        return cartDetailList;
    }

    public void deleteItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());
        if (optionalCart.isEmpty()) {
            return;
        }
        Cart cart = optionalCart.get();
        if (!cartItemRepository.existsCartItemByCart_IdAndBook_Id(cart.getId(), itemId)) {
            return;
        }
        cartItemRepository.deleteCartItemByCart_IdAndBook_Id(cart.getId(), itemId);

    }

    public void deleteAllItem(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException("해당하는 이메일의 유저가 없습니다."));
        Cart userCart =
                cartRepository.findCartByUserId(user.getId()).orElseGet(() -> cartRepository.save(new Cart(user)));
        cartItemRepository.deleteAllByCart_Id(userCart.getId());
    }

    private String getExpiredKey(String cartKey) {
        return EXPIRED_KEY + " " + cartKey;
    }
}
