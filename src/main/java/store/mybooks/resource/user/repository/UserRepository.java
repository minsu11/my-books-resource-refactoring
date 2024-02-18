package store.mybooks.resource.user.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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


    // 받오오는 경우 기본적으로 Optional로 감싸서 받아오기
    // 이렇게 하는경우 중복체크 , Empty체크 등이 유의하며 filer , map 등의 Stream API 기능을 사용 가능함
    Optional<User> findByEmail(String email);

    UserGetResponse queryByEmail(String email);

}
