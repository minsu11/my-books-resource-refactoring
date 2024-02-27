package store.mybooks.resource.book_tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.book_tag.entity.BookTag;

/**
 * packageName    : store.mybooks.resource.book_tag.repository
 * fileName       : BookTagRepository
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
public interface BookTagRepository extends JpaRepository<BookTag, BookTag.Pk> {
}
