package store.mybooks.resource.cart_item.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.front.cart.domain.CartDetail;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.cart_item.dto.CartUserRedisKeyNameRequest;
import store.mybooks.resource.cart.entity.Cart;
import store.mybooks.resource.cart.repository.CartRepository;
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
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final RedisTemplate<String, CartDetail> redisTemplate;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ImageService imageService;


    public void registerMysqlDataFromRedisData(Long userId, CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest) {
        List<CartDetail> cartDetailList =
                redisTemplate.opsForList().range(cartUserRedisKeyNameRequest.getCartKey(), 0, -1);
        if (Objects.isNull(cartDetailList) || cartDetailList.isEmpty()) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());
        Cart userCart = optionalCart.orElseGet(() -> cartRepository.save(new Cart(user)));
        cartItemRepository.deleteAllByCart_Id(userCart.getId());

        for (CartDetail cartDetail : cartDetailList) {
            Book book = bookRepository.findById(cartDetail.getBookId()).orElseThrow();
            CartItem cartItem = new CartItem(cartDetail.getCartDetailAmount(), userCart, book);
            cartItemRepository.save(cartItem);
        }
        redisTemplate.delete(cartUserRedisKeyNameRequest.getCartKey());
    }

    public void registerRedisDataFromMysqlData(Long userId, CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());
        if (optionalCart.isEmpty()) {
            return;
        }
        Cart userCart = optionalCart.get();
        List<CartItem> cartItemList = cartItemRepository.findCartItemsByCart_Id(userCart.getId());
        if (cartItemList.isEmpty()) {
            return;
        }

        for (CartItem cartItem : cartItemList) {
            Image thumbNailImage = imageService.getThumbNailImage(cartItem.getBook().getId());
            redisTemplate.opsForList().rightPush(
                    cartUserRedisKeyNameRequest.getCartKey(),
                    new CartDetail(
                            cartItem.getBook().getId(),
                            cartItem.getAmount(),
                            cartItem.getBook().getName(),
                            imageService.getObject(thumbNailImage.getId()).getFilePathName(),
                            cartItem.getBook().getOriginalCost(), cartItem.getBook().getSaleCost(),
                            cartItem.getBook().getStock(), cartItem.getBook().getBookStatus().getId())
            );

            cartItemRepository.deleteAllByCart_Id(userCart.getId());
        }
    }

}
