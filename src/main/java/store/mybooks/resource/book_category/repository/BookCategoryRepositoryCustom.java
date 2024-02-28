package store.mybooks.resource.book_category.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.book_category.dto.response.BookGetResponse;
import store.mybooks.resource.book_category.dto.response.CategoryGetResponse;

/**
 * packageName    : store.mybooks.resource.book_category.repository
 * fileName       : BookCategoryRepositoryCustom
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@NoRepositoryBean
public interface BookCategoryRepositoryCustom {
    List<CategoryGetResponse> getCategoryListByBookId(long bookId);

    List<BookGetResponse> getBookListByCategoryId(int categoryId);
}
