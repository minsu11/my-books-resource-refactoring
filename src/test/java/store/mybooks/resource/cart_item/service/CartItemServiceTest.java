package store.mybooks.resource.cart_item.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import store.mybooks.front.cart.domain.CartDetail;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.cart.entity.Cart;
import store.mybooks.resource.cart.repository.CartRepository;
import store.mybooks.resource.cart_item.dto.CartUserRedisKeyNameRequest;
import store.mybooks.resource.cart_item.entity.CartItem;
import store.mybooks.resource.cart_item.repository.CartItemRepository;
import store.mybooks.resource.image.dto.response.ImageGetResponse;
import store.mybooks.resource.image.dto.response.ImageResponse;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.service.ImageService;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

/**
 * packageName    : store.mybooks.resource.cart_item.service <br/>
 * fileName       : CartItemServiceTest<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/24/24        Fiat_lux       최초 생성<br/>
 */
@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private RedisTemplate<String, CartDetail> redisTemplate;

    @Mock
    private RedisTemplate<String, String> stringRedisTemplate;

    @Test
    @DisplayName("registerRedisToMysql 메서드 - 사용자와 장바구니가 존재할 때")
    void testRegisterRedisToMysql_UserAndCartExist() {
        String userEmail = "test@naver.com";
        List<CartDetail> cartDetailList = new ArrayList<>();
        CartDetail cartDetail = new CartDetail(1L, 3, "name", "image", 123, 123, 123, "status");
        cartDetailList.add(cartDetail);
        User user = mock(User.class);
        Cart cart = mock(Cart.class);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(Book.class)));

        cartItemService.registerRedisToMysql(userEmail, cartDetailList);

        verify(bookRepository, times(cartDetailList.size())).findById(any(Long.class));
        verify(cartItemRepository, times(cartDetailList.size())).save(any(CartItem.class));
    }

    @Test
    @DisplayName("registerRedisToMysql 메서드 - 사용자 또는 장바구니가 존재하지 않을 때")
    void testRegisterRedisToMysql_UserOrCartNotExist() {
        String userEmail = "test@naver.com";
        List<CartDetail> cartDetailList = new ArrayList<>();
        CartDetail cartDetail = new CartDetail(1L, 3, "name", "image", 123, 123, 123, "status");
        cartDetailList.add(cartDetail);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class,
                () -> cartItemService.registerRedisToMysql(userEmail, cartDetailList));

        verify(cartRepository, never()).findCartByUserId(any(Long.class));
        verify(cartItemRepository, never()).deleteAllByCart_Id(anyLong());
        verify(bookRepository, never()).findById(any(Long.class));
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

//    @Test
//    @DisplayName("")
//    void a() {
//        Long userId = 1L;
//        String cartKey = "cart_key";
//        CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest = new CartUserRedisKeyNameRequest(cartKey);
//        Book b = Book.builder().
//        User user = mock(User.class);
//        Cart cart = mock(Cart.class);
//        Book book = mock(Book.class);
//        CartItem cartItem = new CartItem(1L, 1, cart, book);
//        List<CartItem> cartItemList = new ArrayList<>();
//        cartItemList.add(cartItem);
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(cartRepository.findCartByUserId(anyLong())).thenReturn(Optional.of(cart));
//        when(cartItemRepository.findCartItemsByCart_Id(anyLong())).thenReturn(cartItemList);
//        when(imageService.getThumbNailImage(anyLong())).thenReturn(mock(Image.class));
//        when(imageService.getObject(anyLong())).thenReturn(mock(ImageGetResponse.class));
//
//        cartItemService.registerMysqlToRedis(userId, cartUserRedisKeyNameRequest);
//
//        verify(redisTemplate, times(1)).opsForList().rightPush(anyString(), any(CartDetail.class));
//        verify(redisTemplate, times(1)).delete(any(String.class));
//        verify(redisTemplate, times(1)).opsForList().rightPush(anyString(), any(CartDetail.class));
//        verify(redisTemplate).expire(anyString(), eq(180L), eq(TimeUnit.MINUTES));
//        verify(stringRedisTemplate).expire(anyString(), eq(179L), eq(TimeUnit.MINUTES));
//
//    }


}