package store.mybooks.resource.bookauthor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.bookauthor.entity.BookAuthor;

/**
 * packageName    : store.mybooks.resource.book_author.repository <br/>
 * fileName       : BookAuthorRepository<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthor.Pk> {
    boolean existsByPk_BookId(Long bookId);

    void deleteByPk_BookId(Long bookId);
}
