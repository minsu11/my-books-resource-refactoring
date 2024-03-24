package store.mybooks.resource.bookcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.bookcategory.entity.BookCategory;

/**
 * packageName    : store.mybooks.resource.book_category.repository
 * fileName       : BookCategoryRepository
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategory.Pk> {
    /**
     * methodName : existsByPk_BookId <br>
     * author : damho-lee <br>
     * description : 도서 아이디로 있는지 조회.<br>
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
}
