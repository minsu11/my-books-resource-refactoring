package store.mybooks.resource.wrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.wrap.entity.Wrap;

/**
 * packageName    : store.mybooks.resource.wrap.repository<br>
 * fileName       : WrapRepository<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
public interface WrapRepository extends JpaRepository<Wrap, Integer>, WrapRepositoryCustom {
}
