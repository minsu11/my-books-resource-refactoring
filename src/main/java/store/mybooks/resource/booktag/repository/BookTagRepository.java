package store.mybooks.resource.booktag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.booktag.entity.BookTag;

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
    /**
     * methodName : existsByPk_BookId <br>
     * author : damho-lee <br>
     * description : 도서 아이디로 존재하는지 판별.<br>
     *
     * @param bookId Long
     * @return boolean
     */
    boolean existsByPk_BookId(Long bookId);

    /**
     * methodName : deleteByPk_BookId <br>
     * author : damho-lee <br>
     * description : 도서 아이디로 삭제.<br>
     *
     * @param bookId Long
     */
    void deleteByPk_BookId(Long bookId);

    void deleteByPk_TagId(Integer tagId);
}
