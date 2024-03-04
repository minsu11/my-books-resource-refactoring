package store.mybooks.resource.category.repository;

import java.util.List;
import store.mybooks.resource.category.dto.response.CategoryNameGetResponse;

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
    List<CategoryNameGetResponse> findFullCategoryForBookViewByBookId(Long bookId);
}
