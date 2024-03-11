package store.mybooks.resource.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForBookCreate;
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
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class CategoryRestControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

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

        String content = objectMapper.writeValueAsString(pageable);

        mockMvc.perform(get("/api/categories/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("parentCategory"))
                .andExpect(jsonPath("$.content[0].parentCategoryName", nullValue()))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("childCategory"))
                .andExpect(jsonPath("$.content[1].parentCategoryName").value("parentCategory"))
                .andDo(document("category-get-page",
                        requestFields(
                                fieldWithPath("pageNumber").description("페이지"),
                                fieldWithPath("pageSize").description("사이즈"),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("offset").ignored(),
                                fieldWithPath("paged").ignored(),
                                fieldWithPath("unpaged").ignored()
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("카테고리 아이디"),
                                fieldWithPath("content[].name").description("카테고리 이름"),
                                fieldWithPath("content[].parentCategoryName").description("부모 카테고리 이름").optional(),
                                fieldWithPath("pageable.sort.*").ignored(),
                                fieldWithPath("pageable.*").ignored(),
                                fieldWithPath("totalElements").ignored(),
                                fieldWithPath("totalPages").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("size").ignored(),
                                fieldWithPath("number").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("empty").ignored()
                        )));

        verify(categoryService, times(1)).getCategoriesOrderByParentCategoryIdForAdminPage(any());
    }

    @Test
    @DisplayName("도서 생성할 때 필요한 카테고리 리스트 가져오기")
    void whenCallGetCategoriesForBookCreateForCreatingBook_thenReturnListOfCategoryGetResponseForBookCreate()
            throws Exception {
        List<CategoryGetResponseForBookCreate> list = new ArrayList<>();
        CategoryGetResponseForBookCreate grandParentCategory =
                new CategoryGetResponseForBookCreate(1, "grandParentCategory");
        CategoryGetResponseForBookCreate parentCategory =
                new CategoryGetResponseForBookCreate(2, "grandParentCategory/parentCategory");
        CategoryGetResponseForBookCreate childCategory =
                new CategoryGetResponseForBookCreate(3, "grandParentCategory/parentCategory/childCategory");
        list.add(grandParentCategory);
        list.add(parentCategory);
        list.add(childCategory);

        when(categoryService.getCategoriesForBookCreate()).thenReturn(list);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()))
                .andExpect(jsonPath("$[0].id").value(grandParentCategory.getId()))
                .andExpect(jsonPath("$[0].name").value(grandParentCategory.getName()))
                .andExpect(jsonPath("$[1].id").value(parentCategory.getId()))
                .andExpect(jsonPath("$[1].name").value(parentCategory.getName()))
                .andExpect(jsonPath("$[2].id").value(childCategory.getId()))
                .andExpect(jsonPath("$[2].name").value(childCategory.getName()))
                .andDo(document("category-get-category-list",
                        responseFields(
                                fieldWithPath("[].id").description("카테고리 아이디"),
                                fieldWithPath("[].name").description("전체 카테고리 이름")
                        )));
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
                .andExpect(jsonPath("$[2].parentCategory", nullValue()))
                .andDo(document("category-get-highest-categories",
                        responseFields(
                                fieldWithPath("[].id").description("카테고리 아이디"),
                                fieldWithPath("[].name").description("카테고리 이름"),
                                fieldWithPath("[].parentCategory").description("부모 카테고리")
                        )));
    }

    @Test
    @DisplayName("ParentCategoryId 로 ChildCategoryList 가져오기")
    void givenGetCategoriesByParentCategoryId_whenNormalCase_thenReturnResponseEntity() throws Exception {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        CategoryGetResponse parentCategory = makeCategoryGetResponse(1, null, "parentCategory");
        CategoryGetResponse firstChildCategory = makeCategoryGetResponse(2, parentCategory, "firstChildCategory");
        CategoryGetResponse secondChildCategory = makeCategoryGetResponse(3, parentCategory, "secondChildCategory");

        categoryGetResponseList.add(parentCategory);
        categoryGetResponseList.add(firstChildCategory);
        categoryGetResponseList.add(secondChildCategory);

        when(categoryService.getCategoriesByParentCategoryId(anyInt())).thenReturn(categoryGetResponseList
                .stream()
                .filter(category -> category.getParentCategory() != null)
                .collect(Collectors.toList()));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/parentCategoryId/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].parentCategory.id").value(parentCategory.getId()))
                .andExpect(jsonPath("$[0].id").value(firstChildCategory.getId()))
                .andExpect(jsonPath("$[1].parentCategory.id").value(parentCategory.getId()))
                .andExpect(jsonPath("$[1].id").value(secondChildCategory.getId()))
                .andDo(document("get-category-list-using-ParentCategoryId",
                        pathParameters(
                                parameterWithName("id").description("부모 카테고리 ID")),
                        responseFields(
                                fieldWithPath("[].name").description("카테고리 이름"),
                                fieldWithPath("[].id").description("카테고리 ID"),
                                fieldWithPath("[].parentCategory").description("부모 카테고리"),
                                fieldWithPath("[].parentCategory.parentCategory").description("부모 카테고리"),
                                fieldWithPath("[].parentCategory.id").description("부모 카테고리 ID"),
                                fieldWithPath("[].parentCategory.name").description("부모 카테고리 이름")

                        )

                ));
    }

    @Test//1, firstCategory
    @DisplayName("카테고리 수정을 위한 카테고리 가져오기")
    void givenCategoryId_whenGetCategoryUpdateForm_thenReturnCategoryGetResponseForUpdate() throws Exception {
        CategoryGetResponseForUpdate categoryGetResponseForUpdate = new CategoryGetResponseForUpdate(
                makeCategoryIdNameGetResponse(1, "firstCategory"),
                "levelOneCategoryName",
                "levelTwoCategoryName");

        when(categoryService.getCategoryForUpdate(anyInt())).thenReturn(categoryGetResponseForUpdate);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/categoryId/{id}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetCategory.name").value("firstCategory"))
                .andExpect(jsonPath("$.targetCategory.id").value(1))
                .andExpect(jsonPath("$.levelOneCategoryName").value("levelOneCategoryName"))
                .andExpect(jsonPath("$.levelTwoCategoryName").value("levelTwoCategoryName"))
                .andDo(document("get-category",
                        pathParameters(
                                parameterWithName("id").description("카테고리 ID")
                        ),
                        responseFields(
                                fieldWithPath("targetCategory").description("카테고리"),
                                fieldWithPath("targetCategory.id").description("카테고리 ID"),
                                fieldWithPath("targetCategory.name").description("카테고리 이름"),
                                fieldWithPath("levelOneCategoryName").description("1단계 카테고리 이름"),
                                fieldWithPath("levelTwoCategoryName").description("2단계 카테고리 이름")
                        )
                ));
    }

    @Test
    @DisplayName("도서 상세페이지에서 보여줄 카테고리 이름 가져오기")
    void givenBookId_whenGetCategoryNameForBookView_thenReturnListOfCategoryIdNameGetResponse() throws Exception {
        CategoryIdNameGetResponse firstCategoryIdNameGetResponse = makeCategoryIdNameGetResponse(1, "firstCategory");
        CategoryIdNameGetResponse secondCategoryIdNameGetResponse = makeCategoryIdNameGetResponse(2, "secondCategory");
        CategoryIdNameGetResponse thirdCategoryIdNameGetResponse = makeCategoryIdNameGetResponse(3, "thirdCategory");
        List<CategoryIdNameGetResponse> categoryIdNameGetResponseList = new ArrayList<>();
        categoryIdNameGetResponseList.add(firstCategoryIdNameGetResponse);
        categoryIdNameGetResponseList.add(secondCategoryIdNameGetResponse);
        categoryIdNameGetResponseList.add(thirdCategoryIdNameGetResponse);
        when(categoryService.getCategoryNameForBookView(anyLong())).thenReturn(categoryIdNameGetResponseList);

        mockMvc.perform(get("/api/categories/bookId/{bookId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(categoryIdNameGetResponseList.size()))
                .andExpect(jsonPath("$[0].id").value(firstCategoryIdNameGetResponse.getId()))
                .andExpect(jsonPath("$[0].name").value(firstCategoryIdNameGetResponse.getName()))
                .andExpect(jsonPath("$[1].id").value(secondCategoryIdNameGetResponse.getId()))
                .andExpect(jsonPath("$[1].name").value(secondCategoryIdNameGetResponse.getName()))
                .andExpect(jsonPath("$[2].id").value(thirdCategoryIdNameGetResponse.getId()))
                .andExpect(jsonPath("$[2].name").value(thirdCategoryIdNameGetResponse.getName()));
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
                .andExpect(jsonPath("$.parentCategory").value(categoryCreateResponse.getParentCategory()))
                .andDo(document("category-create",
                        requestFields(
                                fieldWithPath("parentCategoryId").description("바로 윗 단계 부모 카테고리 ID"),
                                fieldWithPath("name").description("카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("parentCategory").description("부모 카테고리").optional(),
                                fieldWithPath("name").description("카테고리 이름")
                        )
                ));
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
                .andExpect(jsonPath("$.name").value(categoryModifyResponse.getName()))
                .andDo(document("category-modify",
                        requestFields(
                                fieldWithPath("name").description("카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("name").description("카테고리 이름"),
                                fieldWithPath("parentCategoryId").description("부모 카테고리 ID"),
                                fieldWithPath("parentCategoryName").description("부모 카테고리 이름")
                        )
                ));
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

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDeleteResponse.getName()))
                .andDo(document("category-delete",
                        pathParameters(
                                parameterWithName("id").description("카테고리 ID")
                        ),
                        responseFields(
                                fieldWithPath("name").description("카테고리 이름")
                        )));
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
        categoryGetResponseList.add(makeCategoryGetResponse(1, null, "firstCategory"));
        categoryGetResponseList.add(makeCategoryGetResponse(2, null, "secondCategory"));
        categoryGetResponseList.add(makeCategoryGetResponse(3, null, "thirdCategory"));
    }

    private CategoryIdNameGetResponse makeCategoryIdNameGetResponse(Integer id, String name) {
        return new CategoryIdNameGetResponse() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    private CategoryGetResponse makeCategoryGetResponse(Integer id, CategoryGetResponse parentCategory, String name) {
        return new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return parentCategory;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}