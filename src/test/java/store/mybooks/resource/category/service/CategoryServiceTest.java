package store.mybooks.resource.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CategoryNameAlreadyExistsException;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.category.repository.CategoryRepository;

/**
 * packageName    : store.mybooks.resource.category.service
 * fileName       : CategoryServiceTest
 * author         : damho-lee
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    @DisplayName("getHighestCategories 메서드 최상위 카테고리들만 가져온다")
    void givenGetHighestCategories_whenNormalCase_thenReturnHighestCategoryGetResponseList() {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        categoryGetResponseList.add(new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return null;
            }

            @Override
            public String getName() {
                return "firstCategory";
            }
        });

        when(categoryRepository.findAllByParentCategoryIsNull()).thenReturn(categoryGetResponseList);
        List<CategoryGetResponse> actual = categoryService.getHighestCategories();

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);

        CategoryGetResponse actualResponse = actual.get(0);

        assertThat(actualResponse.getId()).isEqualTo(1);
        assertThat(actualResponse.getParentCategory()).isNull();
        assertThat(actualResponse.getName()).isEqualTo("firstCategory");
    }

    @Test
    @DisplayName("getCategoriesByPArentCategoryId 메서드 ParentCategoryId 로 Category 들을 가져온다")
    void givenGetCategoriesByParentCategoryId_whenNormalCase_thenReturnCategoryGetResponseList() {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        categoryGetResponseList.add(new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 2;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return new CategoryGetResponse() {
                    @Override
                    public Integer getId() {
                        return 1;
                    }

                    @Override
                    public CategoryGetResponse getParentCategory() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return "parentCategory";
                    }
                };
            }

            @Override
            public String getName() {
                return "childCategory";
            }
        });

        when(categoryRepository.findAllByParentCategory_Id(anyInt())).thenReturn(categoryGetResponseList);
        when(categoryRepository.existsById(anyInt())).thenReturn(true);
        List<CategoryGetResponse> actual = categoryService.getCategoriesByParentCategoryId(1);

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);

        CategoryGetResponse actualChildCategory = actual.get(0);
        assertThat(actualChildCategory.getId()).isEqualTo(2);
        assertThat(actualChildCategory.getName()).isEqualTo("childCategory");

        CategoryGetResponse actualParentCategory = actualChildCategory.getParentCategory();
        assertThat(actualParentCategory.getId()).isEqualTo(1);
        assertThat(actualParentCategory.getName()).isEqualTo("parentCategory");
        assertThat(actualParentCategory.getParentCategory()).isNull();
    }

    @Test
    @DisplayName("getCategoriesByParentCategoryId 메서드 존재하지 않는 ParentCategoryId 의 경우")
    void givenGetCategoriesByParentCategoryId_whenNotExistsParentCategoryId_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(CategoryNotExistsException.class, () -> categoryService.getCategoriesByParentCategoryId(1));
    }

    @Test
    @DisplayName("createCategory 메서드 정상적인 경우")
    void givenCreateCategory_whenNormalCase_thenSaveCategoryAndReturnCategoryCreateResponse() {
        String name = "categoryName";
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(null, name);

        when(categoryRepository.save(any())).thenReturn(new Category(null, name));

        CategoryCreateResponse response = categoryService.createCategory(categoryCreateRequest);

        assertThat(response.getParentCategory()).isNull();
        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("createCategory 메서드 중복된 CategoryName 의 경우")
    void givenCreateCategory_whenDuplicateName_thenThrowCategoryNameAlreadyExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(CategoryNameAlreadyExistsException.class,
                () -> categoryService.createCategory(new CategoryCreateRequest(null, "name")));
    }

    @Test
    @DisplayName("modifyCategory 메서드 정상적인 경우")
    void givenModifyCategory_whenNormalCase_thenReturnCategoryModifyResponse() {
        Category parentCategory = new Category(1, null, null, "parentCategory", LocalDate.now());
        Category childCategory = new Category(2, null, null, "childCategory", LocalDate.now());

        when(categoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findById(childCategory.getId())).thenReturn(Optional.of(childCategory));

        CategoryModifyRequest categoryModifyRequest =
                new CategoryModifyRequest(parentCategory.getId(), "newChildCategory");
        CategoryModifyResponse response = categoryService.modifyCategory(childCategory.getId(), categoryModifyRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("newChildCategory");
        assertThat(response.getParentCategoryId()).isEqualTo(parentCategory.getId());
    }

    @Test
    @DisplayName("modifyCategory 메서드 중복된 CategoryName 의 경우")
    void givenModifyCategory_whenDuplicateName_thenThrowCategoryNameAlreadyExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(true);

        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest(null, "duplicatedName");
        assertThrows(CategoryNameAlreadyExistsException.class,
                () -> categoryService.modifyCategory(1, categoryModifyRequest));
    }

    @Test
    @DisplayName("modifyCategory 메서드 존재하지 않는 CategoryId 의 경우")
    void givenModifyCategory_whenNotExistsCategoryId_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest(null, "categoryName");
        assertThrows(CategoryNotExistsException.class, () -> categoryService.modifyCategory(1, categoryModifyRequest));
    }

    @Test
    @DisplayName("modifyCategory 메서드 존재하지 않는 ParentCategoryId 의 경우")
    void givenModifyCategory_whenNotExistsParentCategoryId_thenThrowCategoryNotExistsException() {
        Integer notExistsParentCategoryId = 1;
        Integer categoryId = 2;
        Category childCategory = new Category(categoryId, null, null, "childCategory", LocalDate.now());

        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(childCategory));
        when(categoryRepository.findById(notExistsParentCategoryId)).thenReturn(Optional.empty());

        CategoryModifyRequest categoryModifyRequest =
                new CategoryModifyRequest(notExistsParentCategoryId, "categoryName");
        assertThrows(CategoryNotExistsException.class,
                () -> categoryService.modifyCategory(categoryId, categoryModifyRequest));
    }

    @Test
    @DisplayName("deleteCategory 메서드 정상적인 경우")
    void givenDeleteCategory_whenExistsCategoryId_thenReturnCategoryDeleteResponse() {
        Category category = new Category(1, null, null, "categoryName", null);

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(anyInt());

        CategoryDeleteResponse response = categoryService.deleteCategory(category.getId());
        assertThat(response.getName()).isEqualTo(category.getName());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    @DisplayName("deleteCategory 메서드 존재하지 않는 CategoryId 의 경우")
    void givenDeleteCategory_whenNotExistsCategoryId_thenThrowCategoryNotExistsException() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CategoryNotExistsException.class, () -> categoryService.deleteCategory(1));
    }
}