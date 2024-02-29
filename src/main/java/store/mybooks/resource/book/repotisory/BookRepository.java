package store.mybooks.resource.book.repotisory;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.book.entity.Book;

/**
 * packageName    : store.mybooks.resource.book.repotisory <br/>
 * fileName       : BookRepository<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    boolean existsByIsbn(String isbn);
}
