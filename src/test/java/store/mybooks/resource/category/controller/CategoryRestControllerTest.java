package store.mybooks.resource.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import store.mybooks.resource.book.dto.response.BookBriefResponseIncludePublishDate;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForBookCreate;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForCategoryView;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForMainView;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForUpdate;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForView;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
import store.mybooks.resource.category.exception.CannotDeleteParentCategoryException;
import store.mybooks.resource.category.exception.CategoryNameAlreadyExistsException;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.category.exception.CategoryValidationException;
import store.mybooks.resource.category.service.CategoryService;
import store.mybooks.resource.image.dto.response.ImageResponse;

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

    @Test
    @DisplayName("ParentCategoryId 로 ChildCategoryList 가져오기 실패 - 존재하지 않는 카테고리")
    void givenNotExistsCategoryId_whenGetCategoriesByParentCategoryId_thenThrowCategoryNotExistsException()
            throws Exception {
        doThrow(new CategoryNotExistsException(1)).when(categoryService).getCategoriesByParentCategoryId(anyInt());

        mockMvc.perform(get("/api/categories/parentCategoryId/{id}", 1))
                .andExpect(status().isNotFound())
                .andDo(document("category-getCategoriesByParentCategoryId-fail-notExistsCategoryId"));
    }

    @Test
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
    @DisplayName("ParentCategoryId 로 ChildCategoryList 가져오기 실패 - 존재하지 않는 카테고리")
    void givenNotExistsCategoryId_whenGetCategoryForUpdate_thenThrowCategoryNotExistsException()
            throws Exception {
        doThrow(new CategoryNotExistsException(1)).when(categoryService).getCategoryForUpdate(anyInt());

        mockMvc.perform(get("/api/categories/categoryId/{id}", 1))
                .andExpect(status().isNotFound())
                .andDo(document("category-getCategoryForUpdate-fail-notExistsCategoryId"));
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
    @DisplayName("getCategoriesForMainView 메서드 테스트")
    void whenGetCategoriesForMainView_thenReturnListOfCategoryGetResponseForMainView() throws Exception {
        CategoryIdNameGetResponse database = new CategoryIdNameGetResponse(3, "데이터베이스");
        CategoryIdNameGetResponse network = new CategoryIdNameGetResponse(4, "네트워크");
        CategoryIdNameGetResponse algorithm = new CategoryIdNameGetResponse(5, "알고리즘");
        List<CategoryIdNameGetResponse> childOfItCategoryList = new ArrayList<>();
        childOfItCategoryList.add(database);
        childOfItCategoryList.add(network);
        childOfItCategoryList.add(algorithm);

        CategoryIdNameGetResponse corporateManagement = new CategoryIdNameGetResponse(6, "기업 경영");
        CategoryIdNameGetResponse marketing = new CategoryIdNameGetResponse(7, "마케팅");
        CategoryIdNameGetResponse investment = new CategoryIdNameGetResponse(8, "투자");
        List<CategoryIdNameGetResponse> childOfEconomyCategoryList = new ArrayList<>();
        childOfEconomyCategoryList.add(corporateManagement);
        childOfEconomyCategoryList.add(marketing);
        childOfEconomyCategoryList.add(investment);

        CategoryGetResponseForMainView it =
                new CategoryGetResponseForMainView(1, "IT", childOfItCategoryList);
        CategoryGetResponseForMainView economy =
                new CategoryGetResponseForMainView(2, "economy", childOfEconomyCategoryList);
        List<CategoryGetResponseForMainView> expect = Arrays.asList(it, economy);

        when(categoryService.getCategoriesForMainView()).thenReturn(expect);

        mockMvc.perform(get("/api/categories/main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expect.size()))
                .andExpect(jsonPath("$[0].parentCategoryId").value(it.getParentCategoryId()))
                .andExpect(jsonPath("$[0].parentCategoryName").value(it.getParentCategoryName()))
                .andExpect(jsonPath("$[0].childCategoryList.size()").value(childOfItCategoryList.size()))
                .andExpect(jsonPath("$[0].childCategoryList[0].id").value(database.getId()))
                .andExpect(jsonPath("$[0].childCategoryList[1].id").value(network.getId()))
                .andExpect(jsonPath("$[0].childCategoryList[2].id").value(algorithm.getId()))
                .andExpect(jsonPath("$[1].parentCategoryId").value(economy.getParentCategoryId()))
                .andExpect(jsonPath("$[1].parentCategoryName").value(economy.getParentCategoryName()))
                .andExpect(jsonPath("$[1].childCategoryList.size()").value(childOfEconomyCategoryList.size()))
                .andExpect(jsonPath("$[1].childCategoryList[0].id").value(corporateManagement.getId()))
                .andExpect(jsonPath("$[1].childCategoryList[1].id").value(marketing.getId()))
                .andExpect(jsonPath("$[1].childCategoryList[2].id").value(investment.getId()));
    }

    @Test
    @DisplayName("getCategoriesForCategoryView 테스트")
    void givenCategoryId_whenGetCategoriesForCategoryView_thenReturnCategoryGetResponseForCategoryView()
            throws Exception {
        String highestCategoryName = "IT";
        String name = "데이터베이스";
        CategoryIdNameGetResponse network = new CategoryIdNameGetResponse(4, "Network");
        CategoryIdNameGetResponse language = new CategoryIdNameGetResponse(5, "Language");
        List<CategoryIdNameGetResponse> levelTwoCategories = Arrays.asList(network, language);
        CategoryIdNameGetResponse mysql = new CategoryIdNameGetResponse(4, "MySQL");
        CategoryIdNameGetResponse mssql = new CategoryIdNameGetResponse(5, "MSSQL");
        List<CategoryIdNameGetResponse> targetCategories = Arrays.asList(mysql, mssql);
        CategoryGetResponseForCategoryView expect =
                new CategoryGetResponseForCategoryView(
                        highestCategoryName,
                        name,
                        levelTwoCategories,
                        targetCategories
                );

        when(categoryService.getCategoriesForCategoryView(anyInt())).thenReturn(expect);

        mockMvc.perform(get("/api/categories/view/{categoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.highestCategoryName").value(highestCategoryName))
                .andExpect(jsonPath("$.name").value(name))
                .andDo(print())
                .andExpect(jsonPath("$.levelTwoCategories.size()").value(levelTwoCategories.size()))
                .andExpect(jsonPath("$.levelTwoCategories[0].id").value(network.getId()))
                .andExpect(jsonPath("$.levelTwoCategories[0].name").value(network.getName()))
                .andExpect(jsonPath("$.levelTwoCategories[1].id").value(language.getId()))
                .andExpect(jsonPath("$.levelTwoCategories[1].name").value(language.getName()))
                .andExpect(jsonPath("$.targetCategories.size()").value(targetCategories.size()))
                .andExpect(jsonPath("$.targetCategories[0].id").value(mysql.getId()))
                .andExpect(jsonPath("$.targetCategories[0].name").value(mysql.getName()))
                .andExpect(jsonPath("$.targetCategories[1].id").value(mssql.getId()))
                .andExpect(jsonPath("$.targetCategories[1].name").value(mssql.getName()));
    }

    @Test
    @DisplayName("getBooksForCategoryView 테스트")
    void givenCategoryIdAndPageable_whenGetBooksForCategoryView_thenReturnPageOfBookBriefResponseIncludePublishDate()
            throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        ImageResponse appleImageResponse = new ImageResponse("path", "apple", "png");
        BookBriefResponseIncludePublishDate appleBook =
                new BookBriefResponseIncludePublishDate(
                        1L,
                        appleImageResponse,
                        "appleBook",
                        10000,
                        5000,
                        LocalDate.now()
                );
        ImageResponse bananaImageResponse = new ImageResponse("path", "apple", "png");
        BookBriefResponseIncludePublishDate bananaBook =
                new BookBriefResponseIncludePublishDate(
                        1L,
                        bananaImageResponse,
                        "bananaBook",
                        10000,
                        5000,
                        LocalDate.now()
                );
        List<BookBriefResponseIncludePublishDate> bookBriefResponseIncludePublishDateList =
                Arrays.asList(appleBook, bananaBook);
        long total = 100L;
        Page<BookBriefResponseIncludePublishDate> expect =
                new PageImpl<>(
                        bookBriefResponseIncludePublishDateList,
                        pageable,
                        total);

        when(categoryService.getBooksForCategoryView(anyInt(), any())).thenReturn(expect);

        mockMvc.perform(get("/api/categories/view/book/{categoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(bookBriefResponseIncludePublishDateList.size()))
                .andExpect(jsonPath("$.content[0].id").value(appleBook.getId()))
                .andExpect(jsonPath("$.content[1].id").value(bananaBook.getId()))
                .andExpect(jsonPath("$.totalPages").value(total / pageable.getPageSize()));
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
                .andDo(document("category-create-fail-validation"))
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(CategoryValidationException.class);
    }

    @Test
    @DisplayName("카테고리 생성  실패- 이미 존재하는 카테고리 이름")
    void givenAlreadyExistsCategoryName_whenCreateCategory_thenThrowCategoryNameAlreadyExistsException()
            throws Exception {
        doThrow(new CategoryNameAlreadyExistsException("IT")).when(categoryService).createCategory(any());
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(null, "IT");
        String content = objectMapper.writeValueAsString(categoryCreateRequest);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("category-create-fail-alreadyExistsCategoryName"));
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

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentCategoryId").value(categoryModifyResponse.getParentCategoryId()))
                .andExpect(jsonPath("$.parentCategoryName").value(categoryModifyResponse.getParentCategoryName()))
                .andExpect(jsonPath("$.name").value(categoryModifyResponse.getName()))
                .andDo(document("category-modify",
                        pathParameters(
                                parameterWithName("id").description("수정하려는 카테고리 아이디")
                        ),
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

        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andDo(document("category-modify-fail-validation",
                        pathParameters(
                                parameterWithName("id").description("수정하려는 카테고리 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("카테고리 이름")
                        )))
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(CategoryValidationException.class);
    }

    @Test
    @DisplayName("카테고리 생성  실패 - 이미 존재하는 카테고리 이름")
    void givenAlreadyExistsCategoryName_whenModifyCategory_thenThrowCategoryNameAlreadyExistsException()
            throws Exception {
        doThrow(new CategoryNameAlreadyExistsException("IT")).when(categoryService).modifyCategory(anyInt(), any());
        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest("IT");
        String content = objectMapper.writeValueAsString(categoryModifyRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("category-modify-fail-alreadyExistsCategoryName",
                        pathParameters(
                                parameterWithName("id").description("수정하려는 카테고리 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("카테고리 이름")
                        )));
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

    @Test
    @DisplayName("카테고리 삭제 실패 - 존재하지 않는 카테고리")
    void givenNotExistsCategoryId_whenDeleteCategory_thenThrowCategoryNotExistsException() throws Exception {
        doThrow(new CategoryNotExistsException(1)).when(categoryService).deleteCategory(anyInt());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{id}", 1))
                .andExpect(status().isNotFound())
                .andDo(document("category-delete-fail-notExistsCategoryId",
                        pathParameters(
                                parameterWithName("id").description("카테고리 ID")
                        )));
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 자식 카테고리가 있는 경우")
    void givenCategoryIdThatHasChildCategory_whenDeleteCategory_thenThrowCannotDeleteParentCategoryException()
            throws Exception {
        doThrow(new CannotDeleteParentCategoryException()).when(categoryService).deleteCategory(anyInt());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{id}", 1))
                .andExpect(status().isBadRequest())
                .andDo(document("category-delete-fail-hasChildCategory",
                        pathParameters(
                                parameterWithName("id").description("카테고리 ID")
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
        return new CategoryIdNameGetResponse(id, name);
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