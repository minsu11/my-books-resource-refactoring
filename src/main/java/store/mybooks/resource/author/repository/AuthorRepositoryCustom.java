package store.mybooks.resource.author.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;

/**
 * packageName    : store.mybooks.resource.author.repository <br/>
 * fileName       : AuthorRepositoryCustom<br/>
 * author         : newjaehun <br/>
 * date           : 2/26/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/26/24        newjaehun       최초 생성<br/>
 */
public interface AuthorRepositoryCustom {

    List<AuthorGetResponse> getAllAuthors();

    Page<AuthorGetResponse> getAllPagedAuthors(Pageable pageable);

    AuthorGetResponse getAuthorInfo(Integer authorId);
}
