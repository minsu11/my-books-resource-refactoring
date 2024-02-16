package store.mybooks.resource.user_grade.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;

/**
 * packageName    : store.mybooks.resource.user.repository
 * fileName       : UserGradeRepository
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public interface UserGradeRepository extends JpaRepository<UserGrade, Integer> {

    Optional<UserGrade> findByName(String name);

}
