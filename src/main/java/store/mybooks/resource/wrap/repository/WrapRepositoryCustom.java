package store.mybooks.resource.wrap.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.wrap.dto.response.WrapResponse;

/**
 * packageName    : store.mybooks.resource.wrap.repository<br>
 * fileName       : WrapRepositoryCustom<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface WrapRepositoryCustom {
    Optional<WrapResponse> findWrapResponseById(Integer id);

    List<WrapResponse> getWrapResponseList();
}
