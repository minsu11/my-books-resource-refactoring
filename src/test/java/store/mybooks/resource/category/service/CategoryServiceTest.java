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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CategoryNameAlreadyExistsException;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.category.mapper.CategoryMapper;
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

@ExtendWith({MockitoExtension.class})
class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryService categoryService;

    @Test
    @DisplayName("getCategoriesOrderByParentCategoryId 메서드 ParentCategoryId 를 기준으로 Category 를 오름차순으로 반환")
    void givenGetCategoriesOrderByParentCategoryId_whenNormalCase_thenReturnListOfCategory() {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        CategoryGetResponse firstCategory = new CategoryGetResponse() {
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
        };

        CategoryGetResponse secondCategory = new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 2;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return firstCategory;
            }

            @Override
            public String getName() {
                return "secondCategory";
            }
        };

        CategoryGetResponse thirdCategory = new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 3;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return secondCategory;
            }

            @Override
            public String getName() {
                return "thirdCategory";
            }
        };

        categoryGetResponseList.add(thirdCategory);
        categoryGetResponseList.add(secondCategory);
        categoryGetResponseList.add(firstCategory);
        categoryGetResponseList.sort((c1, c2) -> {
            if (c1.getParentCategory() == null) {
                return -1;
            } else if (c2.getParentCategory() == null) {
                return 1;
            }

            return c1.getParentCategory().getId() - c2.getParentCategory().getId();
        });

        Pageable pageable = PageRequest.of(0, 2);
        Page<CategoryGetResponse> expectedPage =
                new PageImpl<>(categoryGetResponseList.subList(0, 2), pageable, categoryGetResponseList.size());
        when(categoryRepository.findByOrderByParentCategory_Id(any())).thenReturn(expectedPage);
        Page<CategoryGetResponse> actualPage = categoryService.getCategoriesOrderByParentCategoryId(pageable);
        assertThat(actualPage.getContent().size()).isEqualTo(2);
        assertThat(actualPage.getContent().get(0).getId()).isEqualTo(firstCategory.getId());
        assertThat(actualPage.getContent().get(1).getId()).isEqualTo(secondCategory.getId());
        assertThat(actualPage.getTotalPages()).isEqualTo(2);
    }

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
        CategoryCreateResponse expectedResponse = new CategoryCreateResponse();
        expectedResponse.setParentCategory(null);
        expectedResponse.setName(name);

        when(categoryMapper.createResponse(any())).thenReturn(expectedResponse);
        when(categoryRepository.save(any())).thenReturn(new Category(null, name));

        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(null, name);
        CategoryCreateResponse actualResponse = categoryService.createCategory(categoryCreateRequest);

        assertThat(actualResponse.getParentCategory()).isNull();
        assertThat(actualResponse.getName()).isEqualTo(name);
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
        CategoryModifyResponse expectedResponse = new CategoryModifyResponse();
        expectedResponse.setParentCategoryId(parentCategory.getId());
        expectedResponse.setParentCategoryName(childCategory.getName());

        when(categoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findById(childCategory.getId())).thenReturn(Optional.of(childCategory));
        when(categoryMapper.modifyResponse(any())).thenReturn(expectedResponse);

        CategoryModifyRequest categoryModifyRequest =
                new CategoryModifyRequest(parentCategory.getId(), "newChildCategory");
        expectedResponse.setName(categoryModifyRequest.getName());
        CategoryModifyResponse actualResponse =
                categoryService.modifyCategory(childCategory.getId(), categoryModifyRequest);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getParentCategoryId()).isEqualTo(expectedResponse.getParentCategoryId());
        assertThat(actualResponse.getName()).isEqualTo(categoryModifyRequest.getName());
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
        CategoryDeleteResponse expectedResponse = new CategoryDeleteResponse();
        expectedResponse.setName(category.getName());

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        when(categoryMapper.deleteResponse(any())).thenReturn(expectedResponse);
        doNothing().when(categoryRepository).deleteById(anyInt());

        CategoryDeleteResponse actualResponse = categoryService.deleteCategory(category.getId());
        assertThat(actualResponse.getName()).isEqualTo(category.getName());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    @DisplayName("deleteCategory 메서드 존재하지 않는 CategoryId 의 경우")
    void givenDeleteCategory_whenNotExistsCategoryId_thenThrowCategoryNotExistsException() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CategoryNotExistsException.class, () -> categoryService.deleteCategory(1));
    }
}