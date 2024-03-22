package store.mybooks.resource.category.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
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
public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {
    /**
     * methodName : findByOrderByParentCategory_Id <br>
     * author : damho-lee <br>
     * description : 부모 카테고리 아이디를 기준으로 오름차순 정렬된 페이지 반환.<br>
     *
     * @param pageable Pageable
     * @return page
     */
    Page<CategoryGetResponse> findByOrderByParentCategory_Id(Pageable pageable);

    /**
     * methodName : queryById <br>
     * author : damho-lee <br>
     * description : Id 로 조회. <br>
     *
     * @param categoryId Integer
     * @return CategoryGetResponse
     */
    CategoryGetResponse queryById(Integer categoryId);

    /**
     * methodName : findAllByOrderByParentCategory_Id <br>
     * author : damho-lee <br>
     * description : 부모 카테고리 아이디를 기준으로 오름차순 정렬 된 리스트 반환. <br>
     *
     * @return list
     */
    List<CategoryGetResponse> findAllByOrderByParentCategory_Id();

    /**
     * methodName : findAllByParentCategoryIsNull <br>
     * author : damho-lee <br>
     * description : 1단계 카테고리 반환.<br>
     *
     * @return list
     */
    List<CategoryGetResponse> findAllByParentCategoryIsNull();

    /**
     * methodName : findAllByParentCategory_Id <br>
     * author : damho-lee <br>
     * description : 부모 카테고리 아이디로 자식 카테고리 리스트 반환.<br>
     *
     * @param parentCategoryId Integer
     * @return list
     */
    List<CategoryGetResponse> findAllByParentCategory_Id(Integer parentCategoryId);

    /**
     * methodName : findCategoryById <br>
     * author : damho-lee <br>
     * description : 카테고리 아이디로 조회.<br>
     *
     * @param categoryId Integer
     * @return category id name get response
     */
    CategoryIdNameGetResponse findCategoryById(Integer categoryId);

    /**
     * methodName : countByParentCategory_Id <br>
     * author : damho-lee <br>
     * description : 부모 카테고리 아이디로 자식 카테고리 개수 반환.<br>
     *
     * @param parentCategoryId Integer
     * @return integer
     */
    Integer countByParentCategory_Id(Integer parentCategoryId);

    /**
     * methodName : existsByName <br>
     * author : damho-lee <br>
     * description : 카테고리 이름 존재하는지 조회.<br>
     *
     * @param name String
     * @return boolean
     */
    boolean existsByName(String name);
}
