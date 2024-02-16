package store.mybooks.resource.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CategoryNameAlreadyExistsException;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
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

    /**
     * methodName : getCategoriesByParentCategoryId
     * author : damho-lee
     * description : parentCategory 의 id 를 통해 list 를 반환.
     *
     * @param id parentCategoryId.
     * @return list
     */
    @Transactional(readOnly = true)
    public List<CategoryGetResponse> getCategoriesByParentCategoryId(int id) {
        if (!categoryRepository.existsById((Integer) id)) {
            throw new CategoryNotExistsException();
        }

        return categoryRepository.findAllByParentCategory_Id(id);
    }

    /**
     * methodName : createCategory
     * author : damho-lee
     * description : CategoryRequest 로 category 를 저장하는 메서드 카테고리 이름이 이미 존재하는 경우 CategoryNameAlreadyExistsException 발생.
     *
     * @param categoryCreateRequest ParentCategory, name 포함.
     * @return category create response
     */
    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        if (categoryRepository.existsByName(categoryCreateRequest.getName())) {
            throw new CategoryNameAlreadyExistsException();
        }

        return categoryRepository.save(new Category(categoryCreateRequest)).convertToCategoryCreateResponse();
    }
}
