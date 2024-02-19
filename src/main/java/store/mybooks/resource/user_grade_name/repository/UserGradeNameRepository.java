package store.mybooks.resource.user_grade_name.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;

/**
 * packageName    : store.mybooks.resource.user_grade_name.repository
 * fileName       : UserGradeNameRepository
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public interface UserGradeNameRepository extends JpaRepository<UserGradeName, String> {
}
