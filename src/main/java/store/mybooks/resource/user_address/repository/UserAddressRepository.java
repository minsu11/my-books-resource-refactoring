package store.mybooks.resource.user_address.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.entity.UserAddress;

/**
 * packageName    : store.mybooks.resource.user_address.repository<br>
 * fileName       : UserAddressRepository<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {


    Optional<UserAddressGetResponse> queryByIdAndUserId(Long userAddressId, Long userId);

    List<UserAddressGetResponse> queryAllByUserId(Long userId);

    Page<UserAddressGetResponse> queryAllBy(Pageable pageable);

    Integer countByUserId(Long userId);


}
