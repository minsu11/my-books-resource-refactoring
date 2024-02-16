package store.mybooks.resource.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.repository.CategoryRepository;

/**
 * packageName    : store.mybooks.resource.category.service
 * fileName       : CategoryService
 * author         : damho
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryGetResponse> getHighestCategories() {
        return categoryRepository.findAllByParentCategoryIsNull();
    }

    @Transactional(readOnly = true)
    public List<CategoryGetResponse> getCategoriesByParentCategoryId(int id) {
        return categoryRepository.findAllByParentCategory_Id(id);
    }

    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        return categoryRepository.save(new Category(categoryCreateRequest)).convertToCategoryCreateResponse();
    }
}
