package store.mybooks.resource.book_status.respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.book_status.dto.response.BookStatusGetResponse;
import store.mybooks.resource.book_status.entity.BookStatus;

/**
 * packageName    : store.mybooks.resource.book_status.respository
 * fileName       : BookStatusRepository
 * author         : newjaehun
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24        newjaehun       최초 생성
 */
public interface BookStatusRepository extends JpaRepository<BookStatus, String> {
    List<BookStatusGetResponse> findAllBy();
}
