package store.mybooks.resource.category.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.entity.Category;

/**
 * packageName    : store.mybooks.resource.category.repository
 * fileName       : CategoryRepository
 * author         : damho
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<CategoryGetResponse> findAllByParentCategoryIsNull();

    List<CategoryGetResponse> findAllByParentCategory_Id(int id);
}
