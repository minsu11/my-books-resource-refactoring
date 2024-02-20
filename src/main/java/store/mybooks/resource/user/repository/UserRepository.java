package store.mybooks.resource.user.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user.repository
 * fileName       : UserRepository
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public interface UserRepository extends JpaRepository<User, Long> {



    Optional<User> findByEmail(String email);

    UserGetResponse queryByEmail(String email);

    Optional<UserGetResponse> queryById(Long id);

    Page<UserGetResponse> queryAllBy(Pageable pageable);

}
