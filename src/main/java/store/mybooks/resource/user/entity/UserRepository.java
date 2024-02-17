package store.mybooks.resource.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : store.mybooks.resource.user.entity
 * fileName       : UserRepository
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
