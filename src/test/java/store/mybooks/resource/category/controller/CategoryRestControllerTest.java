package store.mybooks.resource.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForUpdate;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForView;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
import store.mybooks.resource.category.exception.CategoryValidationException;
import store.mybooks.resource.category.service.CategoryService;

/**
 * packageName    : store.mybooks.resource.category.controller
 * fileName       : CategoryRestControllerTest
 * author         : damho-lee
 * date           : 2/21/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/21/24          damho-lee          최초 생성
 */
@WebMvcTest(value = CategoryRestController.class)
class CategoryRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;

    @Test
    @DisplayName("ParentCategoryId 를 기준으로 오름차순 정렬된 Page<CategoryGetResponse> 반환")
    void givenGetCategoriesOrderByParentCategoryId_whenNormalCase_thenReturnResponseEntity() throws Exception {
        List<CategoryGetResponseForView> categoryGetResponseList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 2);
        initCategoryGetResponseList(categoryGetResponseList);
        Page<CategoryGetResponseForView> categoryGetResponsePage =
                new PageImpl<>(categoryGetResponseList.subList(0, 2), pageable, 1);

        when(categoryService.getCategoriesOrderByParentCategoryIdForAdminPage(any())).thenReturn(
                categoryGetResponsePage);

        System.out.println("size : " + categoryGetResponseList.size());

        mockMvc.perform(get("/api/categories/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("parentCategory"))
                .andExpect(jsonPath("$.content[0].parentCategoryName", nullValue()))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("childCategory"))
                .andExpect(jsonPath("$.content[1].parentCategoryName").value("parentCategory"));
    }

    @Test
    @DisplayName("최상위 카테고리들 가져오기")
    void givenGetHighestCategories_whenNormalCase_thenReturnResponseEntity() throws Exception {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        initHighestCategoryList(categoryGetResponseList);
        when(categoryService.getHighestCategories()).thenReturn(categoryGetResponseList);

        mockMvc.perform(get("/api/categories/highest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("firstCategory"))
                .andExpect(jsonPath("$[0].parentCategory", nullValue()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("secondCategory"))
                .andExpect(jsonPath("$[1].parentCategory", nullValue()))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("thirdCategory"))
                .andExpect(jsonPath("$[2].parentCategory", nullValue()));
    }

    @Test
    @DisplayName("ParentCategoryId 로 ChildCategoryList 가져오기")
    void givenGetCategoriesByParentCategoryId_whenNormalCase_thenReturnResponseEntity() throws Exception {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        Integer parentCategoryId = 1;
        initChildCategoryList(categoryGetResponseList, parentCategoryId);
        when(categoryService.getCategoriesByParentCategoryId(anyInt())).thenReturn(categoryGetResponseList);

        mockMvc.perform(get("/api/categories/parentCategoryId/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].parentCategory.id").value(parentCategoryId))
                .andExpect(jsonPath("$[1].parentCategory.id").value(parentCategoryId));
    }

    @Test
    @DisplayName("카테고리 수정을 위한 카테고리 가져오기")
    void givenCategoryId_whenGetCategoryUpdateForm_thenReturnCategoryGetResponseForUpdate() throws Exception {
        CategoryGetResponseForUpdate categoryGetResponseForUpdate = new CategoryGetResponseForUpdate(
                new CategoryIdNameGetResponse() {
                    @Override
                    public Integer getId() {
                        return 1;
                    }

                    @Override
                    public String getName() {
                        return "firstCategory";
                    }
                }, "levelOneCategoryName", "levelTwoCategoryName");

        when(categoryService.getCategoryForUpdate(anyInt())).thenReturn(categoryGetResponseForUpdate);

        mockMvc.perform(get("/api/categories/categoryId/{id}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetCategory.name").value("firstCategory"))
                .andExpect(jsonPath("$.targetCategory.id").value(1))
                .andExpect(jsonPath("$.levelOneCategoryName").value("levelOneCategoryName"))
                .andExpect(jsonPath("$.levelTwoCategoryName").value("levelTwoCategoryName"));
    }

    @Test
    @DisplayName("카테고리 생성")
    void givenCreateCategory_whenNormalCase_thenReturnIsCreated() throws Exception {
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(null, "categoryName");
        CategoryCreateResponse categoryCreateResponse = new CategoryCreateResponse();
        categoryCreateResponse.setName(categoryCreateRequest.getName());
        categoryCreateResponse.setParentCategory(null);
        when(categoryService.createCategory(any())).thenReturn(categoryCreateResponse);

        String content = objectMapper.writeValueAsString(categoryCreateRequest);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(categoryCreateResponse.getName()))
                .andExpect(jsonPath("$.parentCategory").value(categoryCreateResponse.getParentCategory()));
    }

    @Test
    @DisplayName("카테고리 생성 - Validation 실패")
    void givenCreateCategory_whenValidationFailure_thenReturnBadRequest() throws Exception {
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(null, null);

        String content = objectMapper.writeValueAsString(categoryCreateRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(CategoryValidationException.class);
    }


    @Test
    @DisplayName("카테고리 수정")
    void givenModifyCategory_whenNormalCase_thenReturnIsOk() throws Exception {
        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest("categoryName");
        CategoryModifyResponse categoryModifyResponse = new CategoryModifyResponse();
        categoryModifyResponse.setParentCategoryName(null);
        categoryModifyResponse.setParentCategoryId(null);
        categoryModifyResponse.setName(categoryModifyRequest.getName());
        when(categoryService.modifyCategory(anyInt(), any())).thenReturn(categoryModifyResponse);

        String content = objectMapper.writeValueAsString(categoryModifyRequest);

        mockMvc.perform(put("/api/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentCategoryId").value(categoryModifyResponse.getParentCategoryId()))
                .andExpect(jsonPath("$.parentCategoryName").value(categoryModifyResponse.getParentCategoryName()))
                .andExpect(jsonPath("$.name").value(categoryModifyResponse.getName()));
    }

    @Test
    @DisplayName("카테고리 수정 - Validation 실패")
    void givenModifyCategory_whenValidationFailure_thenReturnBadRequest() throws Exception {
        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest("   ");
        String content = objectMapper.writeValueAsString(categoryModifyRequest);

        MvcResult mvcResult = mockMvc.perform(put("/api/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(CategoryValidationException.class);
    }

    @Test
    @DisplayName("카테고리 삭제")
    void givenDeleteCategory_whenNormalCase_thenReturnIsOk() throws Exception {
        CategoryDeleteResponse categoryDeleteResponse = new CategoryDeleteResponse();
        categoryDeleteResponse.setName("IT");

        when(categoryService.deleteCategory(anyInt())).thenReturn(categoryDeleteResponse);

        mockMvc.perform(delete("/api/categories/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDeleteResponse.getName()));
    }

    private void initCategoryGetResponseList(List<CategoryGetResponseForView> categoryGetResponseList) {
        CategoryGetResponseForView parentCategory = new CategoryGetResponseForView(1, "parentCategory", null);

        CategoryGetResponseForView childCategory =
                new CategoryGetResponseForView(2, "childCategory", parentCategory.getName());

        CategoryGetResponseForView category = new CategoryGetResponseForView(3, "category", null);

        categoryGetResponseList.add(parentCategory);
        categoryGetResponseList.add(childCategory);
        categoryGetResponseList.add(category);
    }

    private void initHighestCategoryList(List<CategoryGetResponse> categoryGetResponseList) {
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

        categoryGetResponseList.add(new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 2;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return null;
            }

            @Override
            public String getName() {
                return "secondCategory";
            }
        });

        categoryGetResponseList.add(new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 3;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return null;
            }

            @Override
            public String getName() {
                return "thirdCategory";
            }
        });
    }

    private void initChildCategoryList(List<CategoryGetResponse> categoryGetResponseList, Integer parentCategoryId) {
        CategoryGetResponse parentCategory = new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return parentCategoryId;
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
        categoryGetResponseList.add(new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 2;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return parentCategory;
            }

            @Override
            public String getName() {
                return "firstChildCategory";
            }
        });

        categoryGetResponseList.add(new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return 3;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return parentCategory;
            }

            @Override
            public String getName() {
                return "secondChildCategory";
            }
        });
    }
}