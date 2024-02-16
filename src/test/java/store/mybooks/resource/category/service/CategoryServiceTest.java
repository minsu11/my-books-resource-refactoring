package store.mybooks.resource.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
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
    void givenCategory_whenFindHighestCategories_thenReturnHighestCategoryGetResponseList() {
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
    void givenCategory_whenFindCategoryListByParentCategoryId_thenReturnCategoryGetResponseList() {
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
    void givenCategory_whenCreateCategory_thenSaveCategoryAndReturnCategoryCreateResponse() {
        String name = "categoryName";

        when(categoryRepository.save(any())).thenReturn(
                CategoryCreateResponse.builder()
                        .parentCategory(null)
                        .name(name)
                        .build());

        CategoryCreateResponse response =
                categoryService.createCategory(new CategoryCreateRequest(null, name));

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getParentCategory()).isNull();
    }
}