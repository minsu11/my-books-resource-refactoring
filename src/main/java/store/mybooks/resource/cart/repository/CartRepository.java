package store.mybooks.resource.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.cart.entity.Cart;

/**
 * packageName    : store.mybooks.resource.cart.repository
 * fileName       : CartRepository
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * methodName : findCartByUserId<br>
     * author : Fiat_lux<br>
     * description : cart read by UserId
     *
     * @param userId the user id
     * @return the optional Cart
     */
    Optional<Cart> findCartByUserId(Long userId);
}
