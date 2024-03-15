package store.mybooks.resource.category.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.book.dto.response.BookBriefResponseIncludePublishDate;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForQuerydsl;

/**
 * packageName    : store.mybooks.resource.category.repository
 * fileName       : CategoryRepositoryCustom
 * author         : damho-lee
 * date           : 3/3/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/3/24          damho-lee          최초 생성
 */
public interface CategoryRepositoryCustom {
    /**
     * methodName : findFullCategoryForBookViewByBookId <br>
     * author : damho-lee <br>
     * description : 도서 상세페이지에서 보여줄 카테고리 이름 리스트 조회.<br>
     *
     * @param bookId Long
     * @return list
     */
    List<CategoryGetResponseForQuerydsl> findFullCategoryForBookViewByBookId(Long bookId);

    /**
     * methodName : findHighestCategoryId <br>
     * author : damho-lee <br>
     * description : 카테고리 아이디의 최상위 카테고리 검색.<br>
     *
     * @param categoryId Integer
     * @return integer
     */
    Integer findHighestCategoryId(Integer categoryId);

    /**
     * methodName : getBooksForCategoryView <br>
     * author : damho-lee <br>
     * description : 카테고리에 속하는 도서들 조회.<br>
     *
     * @param categoryId Integer
     * @param pageable   Pageable
     * @return page
     */
    Page<BookBriefResponseIncludePublishDate> getBooksForCategoryView(Integer categoryId, Pageable pageable);
}
