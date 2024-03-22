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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import store.mybooks.resource.book.dto.response.BookBriefResponseIncludePublishDate;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForBookCreate;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForCategoryView;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForMainView;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForQuerydsl;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForUpdate;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForView;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CannotDeleteParentCategoryException;
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
        CategoryGetResponse firstCategory = makeCategoryGetResponse(1, null, "firstCategory");
        CategoryGetResponse secondCategory = makeCategoryGetResponse(2, firstCategory, "secondCategory");
        CategoryGetResponse thirdCategory = makeCategoryGetResponse(3, secondCategory, "thirdCategory");

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
        assertThat(actualPage.getContent()).hasSize(2);
        assertThat(actualPage.getContent().get(0).getId()).isEqualTo(firstCategory.getId());
        assertThat(actualPage.getContent().get(1).getId()).isEqualTo(secondCategory.getId());
        assertThat(actualPage.getTotalPages()).isEqualTo(2);
        verify(categoryRepository, times(1)).findByOrderByParentCategory_Id(any());
    }

    @Test
    @DisplayName("ParentCategoryId 를 기준으로 오름차순 정렬된 category 리스트 반환")
    void givenPageable_whenGetCategoryListOrderByParentCategoryId_thenReturnPageOfCategoryGetResponseForView() {
        Pageable pageable = PageRequest.of(0, 3);
        List<CategoryGetResponse> categoryGetResponseForViewList = new ArrayList<>();
        CategoryGetResponse grandParentCategoryGetResponse =
                makeCategoryGetResponse(1, null, "grandParentCategory");
        CategoryGetResponse parentCategoryGetResponse =
                makeCategoryGetResponse(2, grandParentCategoryGetResponse, "parentCategory");
        CategoryGetResponse childCategoryGetResponse =
                makeCategoryGetResponse(3, parentCategoryGetResponse, "childCategory");
        categoryGetResponseForViewList.add(grandParentCategoryGetResponse);
        categoryGetResponseForViewList.add(parentCategoryGetResponse);
        categoryGetResponseForViewList.add(childCategoryGetResponse);

        PageImpl<CategoryGetResponse> categoryGetResponseForViewPage =
                new PageImpl<>(categoryGetResponseForViewList, pageable, 3);

        when(categoryRepository.findByOrderByParentCategory_Id(any())).thenReturn(categoryGetResponseForViewPage);

        Page<CategoryGetResponseForView> actualPage =
                categoryService.getCategoriesOrderByParentCategoryIdForAdminPage(pageable);

        assertThat(actualPage).isNotNull().hasSize(3);
        List<CategoryGetResponseForView> actualList = actualPage.getContent();
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getParentCategoryName()).isEmpty();
        assertThat(actualList.get(0).getId()).isEqualTo(1);
        assertThat(actualList.get(0).getName()).isEqualTo("grandParentCategory");
        assertThat(actualList.get(1).getParentCategoryName()).isEqualTo("grandParentCategory");
        assertThat(actualList.get(1).getId()).isEqualTo(2);
        assertThat(actualList.get(1).getName()).isEqualTo("parentCategory");
        assertThat(actualList.get(2).getParentCategoryName()).isEqualTo("grandParentCategory > parentCategory");
        assertThat(actualList.get(2).getId()).isEqualTo(3);
        assertThat(actualList.get(2).getName()).isEqualTo("childCategory");
        verify(categoryRepository, times(1)).findByOrderByParentCategory_Id(any());
    }

    @Test
    @DisplayName("도서 생성할 때 필요한 카테고리 리스트 반환")
    void whenCallGetCategoriesForBookCreateForBookCreate_thenReturnListOfCategoryGetResponseForBookCreate() {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        CategoryGetResponse firstCategory = makeCategoryGetResponse(1, null, "firstCategory");
        CategoryGetResponse secondCategory = makeCategoryGetResponse(2, firstCategory, "secondCategory");
        CategoryGetResponse thirdCategory = makeCategoryGetResponse(3, secondCategory, "thirdCategory");
        CategoryGetResponse idIsNullCategory = makeCategoryGetResponse(null, firstCategory, "idIsNullCategory");

        categoryGetResponseList.add(firstCategory);
        categoryGetResponseList.add(secondCategory);
        categoryGetResponseList.add(thirdCategory);
        categoryGetResponseList.add(idIsNullCategory);
        categoryGetResponseList.add(null);

        when(categoryRepository.findAllByOrderByParentCategory_Id()).thenReturn(categoryGetResponseList);

        List<CategoryGetResponseForBookCreate> actualList = categoryService.getCategoriesForBookCreate();
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getId()).isEqualTo(firstCategory.getId());
        assertThat(actualList.get(0).getName()).isEqualTo(firstCategory.getName());
        assertThat(actualList.get(1).getId()).isEqualTo(secondCategory.getId());
        assertThat(actualList.get(1).getName()).isEqualTo(
                firstCategory.getName().concat(" > ").concat(secondCategory.getName()));
        assertThat(actualList.get(2).getId()).isEqualTo(thirdCategory.getId());
        assertThat(actualList.get(2).getName()).isEqualTo(
                firstCategory.getName().concat(" > ")
                        .concat(secondCategory.getName()).concat(" > ")
                        .concat(thirdCategory.getName()));
        verify(categoryRepository, times(1)).findAllByOrderByParentCategory_Id();
    }

    @Test
    @DisplayName("도서 상세페이지에서 보여줄 카테고리 이름 조회")
    void givenBookId_whenGetCategoryNameForBookView_thenReturnListOfCategoryIdNameGetResponse() {
        List<CategoryGetResponseForQuerydsl> categoryNameGetResponseList = new ArrayList<>();
        CategoryGetResponseForQuerydsl levelOneCategory = new CategoryGetResponseForQuerydsl(
                1,
                null,
                null,
                "IT"
        );

        CategoryGetResponseForQuerydsl levelTwoCategory = new CategoryGetResponseForQuerydsl(
                22,
                null,
                "경제경영",
                "경제"
        );

        CategoryGetResponseForQuerydsl levelThreeCategory = new CategoryGetResponseForQuerydsl(
                33,
                "문학 소설",
                "소설",
                "로맨스"
        );
        categoryNameGetResponseList.add(levelOneCategory);
        categoryNameGetResponseList.add(levelTwoCategory);
        categoryNameGetResponseList.add(levelThreeCategory);
        when(categoryRepository.findFullCategoryForBookViewByBookId(1L)).thenReturn(categoryNameGetResponseList);

        List<CategoryIdNameGetResponse> actualList = categoryService.getCategoryNameForBookView(1L);
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getId()).isEqualTo(levelOneCategory.getId());
        assertThat(actualList.get(0).getName()).isEqualTo(levelOneCategory.getName3());
        assertThat(actualList.get(1).getId()).isEqualTo(levelTwoCategory.getId());
        assertThat(actualList.get(1).getName()).isEqualTo(
                levelTwoCategory.getName2() + " > " + levelTwoCategory.getName3());
        assertThat(actualList.get(2).getId()).isEqualTo(levelThreeCategory.getId());
        assertThat(actualList.get(2).getName()).isEqualTo(
                levelThreeCategory.getName1() + " > " + levelThreeCategory.getName2() + " > " +
                        levelThreeCategory.getName3());
    }

    @Test
    @DisplayName("카테고리 업데이트 시에 해당 카테고리의 부모 카테고리 이름 변경해서 가져오기")
    void givenCategoryId_whenGetCategoryForUpdate_thenReturnReNamingCategoryGetResponseForUpdate() {
        CategoryGetResponse grandParentCategory = makeCategoryGetResponse(1, null, "grandParentCategory");
        CategoryGetResponse parentCategory = makeCategoryGetResponse(2, grandParentCategory, "parentCategory");
        CategoryGetResponse childCategory = makeCategoryGetResponse(3, parentCategory, "childCategory");
        when(categoryRepository.existsById(3)).thenReturn(true);
        when(categoryRepository.queryById(3)).thenReturn(childCategory);

        CategoryGetResponseForUpdate actual = categoryService.getCategoryForUpdate(3);

        assertThat(actual).isNotNull();
        assertThat(actual.getTargetCategory().getId()).isEqualTo(childCategory.getId());
        assertThat(actual.getTargetCategory().getName()).isEqualTo(childCategory.getName());
        assertThat(actual.getLevelOneCategoryName()).isEqualTo(grandParentCategory.getName());
        assertThat(actual.getLevelTwoCategoryName()).isEqualTo(parentCategory.getName());
        verify(categoryRepository, times(1)).existsById(3);
        verify(categoryRepository, times(1)).queryById(3);
    }

    @Test
    @DisplayName("CategoryGetResponseForUpdate 조회 시 존재하지 않는 카테고리 Id 를 넘겨준 경우")
    void givenNotExistsCategoryId_whenGetCategoryForUpdate_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsById(any())).thenReturn(false);
        assertThrows(CategoryNotExistsException.class, () -> categoryService.getCategoryForUpdate(1));
        verify(categoryRepository, times(1)).existsById(1);
    }


    @Test
    @DisplayName("getHighestCategories 메서드 최상위 카테고리들만 가져온다")
    void givenGetHighestCategories_whenNormalCase_thenReturnHighestCategoryGetResponseList() {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        CategoryGetResponse firstCategory = makeCategoryGetResponse(1, null, "firstCategory");
        CategoryGetResponse secondCategory = makeCategoryGetResponse(2, firstCategory, "secondCategory");
        CategoryGetResponse thirdCategory = makeCategoryGetResponse(3, secondCategory, "thirdCategory");
        categoryGetResponseList.add(firstCategory);
        categoryGetResponseList.add(secondCategory);
        categoryGetResponseList.add(thirdCategory);

        when(categoryRepository.findAllByParentCategoryIsNull())
                .thenReturn(categoryGetResponseList.stream()
                        .filter(category -> category.getParentCategory() == null)
                        .collect(Collectors.toList()));
        List<CategoryGetResponse> actual = categoryService.getHighestCategories();

        assertThat(actual).isNotNull().hasSize(1);

        CategoryGetResponse actualResponse = actual.get(0);

        assertThat(actualResponse.getId()).isEqualTo(1);
        assertThat(actualResponse.getParentCategory()).isNull();
        assertThat(actualResponse.getName()).isEqualTo("firstCategory");
        verify(categoryRepository, times(1)).findAllByParentCategoryIsNull();
    }

    @Test
    @DisplayName("getCategoriesByPArentCategoryId 메서드 ParentCategoryId 로 Category 들을 가져온다")
    void givenGetCategoriesByParentCategoryId_whenNormalCase_thenReturnCategoryGetResponseList() {
        List<CategoryGetResponse> categoryGetResponseList = new ArrayList<>();
        categoryGetResponseList.add(makeCategoryGetResponse(
                2,
                makeCategoryGetResponse(1, null, "parentCategory"),
                "childCategory"
        ));

        when(categoryRepository.findAllByParentCategory_Id(anyInt())).thenReturn(categoryGetResponseList);
        when(categoryRepository.existsById(anyInt())).thenReturn(true);
        List<CategoryGetResponse> actual = categoryService.getCategoriesByParentCategoryId(1);

        assertThat(actual).isNotNull().hasSize(1);

        CategoryGetResponse actualChildCategory = actual.get(0);
        assertThat(actualChildCategory.getId()).isEqualTo(2);
        assertThat(actualChildCategory.getName()).isEqualTo("childCategory");

        CategoryGetResponse actualParentCategory = actualChildCategory.getParentCategory();
        assertThat(actualParentCategory.getId()).isEqualTo(1);
        assertThat(actualParentCategory.getName()).isEqualTo("parentCategory");
        assertThat(actualParentCategory.getParentCategory()).isNull();
        verify(categoryRepository, times(1)).findAllByParentCategory_Id(1);
        verify(categoryRepository, times(1)).existsById(1);
    }

    @Test
    @DisplayName("getCategoriesByParentCategoryId 메서드 존재하지 않는 ParentCategoryId 의 경우")
    void givenGetCategoriesByParentCategoryId_whenNotExistsParentCategoryId_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(CategoryNotExistsException.class, () -> categoryService.getCategoriesByParentCategoryId(1));
        verify(categoryRepository, times(1)).existsById(1);
    }

    @Test
    @DisplayName("getCategoriesForMainView 테스트")
    void whenGetCategoriesForMainView_thenReturnListOfCategoryGetResponseForMainView() {
        CategoryGetResponse it = makeCategoryGetResponse(1, null, "IT");
        CategoryGetResponse economy = makeCategoryGetResponse(2, null, "경제");
        CategoryGetResponse novel = makeCategoryGetResponse(3, null, "소설");
        List<CategoryGetResponse> highestCategoryList = Arrays.asList(it, economy, novel);

        CategoryGetResponse database = makeCategoryGetResponse(4, it, "데이터베이스");
        CategoryGetResponse network = makeCategoryGetResponse(5, it, "네트워크");
        CategoryGetResponse investment = makeCategoryGetResponse(6, economy, "투자");
        CategoryGetResponse marketing = makeCategoryGetResponse(7, economy, "마케팅");
        CategoryGetResponse genre = makeCategoryGetResponse(8, novel, "장르");
        CategoryGetResponse classic = makeCategoryGetResponse(9, novel, "고전");

        List<CategoryGetResponse> childOfIt = Arrays.asList(database, network);
        List<CategoryGetResponse> childOfEconomy = Arrays.asList(investment, marketing);
        List<CategoryGetResponse> childOfNovel = Arrays.asList(genre, classic);

        when(categoryRepository.findAllByParentCategoryIsNull()).thenReturn(highestCategoryList);
        when(categoryRepository.findAllByParentCategory_Id(1)).thenReturn(childOfIt);
        when(categoryRepository.findAllByParentCategory_Id(2)).thenReturn(childOfEconomy);
        when(categoryRepository.findAllByParentCategory_Id(3)).thenReturn(childOfNovel);

        List<CategoryGetResponseForMainView> actualList = categoryService.getCategoriesForMainView();
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getParentCategoryId()).isEqualTo(it.getId());
        assertThat(actualList.get(0).getParentCategoryName()).isEqualTo(it.getName());
        assertThat(actualList.get(0).getChildCategoryList()).isNotNull().hasSize(2);
        assertThat(actualList.get(0).getChildCategoryList().get(0).getId()).isEqualTo(database.getId());
        assertThat(actualList.get(0).getChildCategoryList().get(1).getId()).isEqualTo(network.getId());

        assertThat(actualList.get(1).getParentCategoryId()).isEqualTo(economy.getId());
        assertThat(actualList.get(1).getParentCategoryName()).isEqualTo(economy.getName());
        assertThat(actualList.get(1).getChildCategoryList()).isNotNull().hasSize(2);
        assertThat(actualList.get(1).getChildCategoryList().get(0).getId()).isEqualTo(investment.getId());
        assertThat(actualList.get(1).getChildCategoryList().get(1).getId()).isEqualTo(marketing.getId());

        assertThat(actualList.get(2).getParentCategoryId()).isEqualTo(novel.getId());
        assertThat(actualList.get(2).getParentCategoryName()).isEqualTo(novel.getName());
        assertThat(actualList.get(2).getChildCategoryList()).isNotNull().hasSize(2);
        assertThat(actualList.get(2).getChildCategoryList().get(0).getId()).isEqualTo(genre.getId());
        assertThat(actualList.get(2).getChildCategoryList().get(1).getId()).isEqualTo(classic.getId());

        verify(categoryRepository, times(1)).findAllByParentCategoryIsNull();
        verify(categoryRepository, times(3)).findAllByParentCategory_Id(anyInt());
    }

    @Test
    @DisplayName("getCategoriesForMainView - 카테고리 아이디에 해당하는 카테고리가 없는 경우")
    void givenNotExistsCategoryId_whenGetCategoriesForMainView_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(CategoryNotExistsException.class, () -> categoryService.getCategoriesForCategoryView(1));
    }

    @Test
    @DisplayName("getBooksForCategoryView")
    void givenCategoryIdAndPageable_whenGetBooksForCategoryView_thenReturnPageOfBookBriefResponseIncludePublishDate() {
        BookBriefResponseIncludePublishDate appleBook =
                new BookBriefResponseIncludePublishDate(
                        1L,
                        null,
                        "appleBook",
                        50D,
                        100L,
                        10000,
                        5000,
                        LocalDate.of(2024, 1, 1));
        BookBriefResponseIncludePublishDate bananaBook =
                new BookBriefResponseIncludePublishDate(
                        2L,
                        null,
                        "bananaBook",
                        10D,
                        30L,
                        10000,
                        9000,
                        LocalDate.of(2022, 12, 15));
        BookBriefResponseIncludePublishDate grapeBook =
                new BookBriefResponseIncludePublishDate(
                        3L,
                        null,
                        "grapeBook",
                        20D,
                        10L,
                        10000,
                        8000,
                        LocalDate.of(2021, 10, 24));
        Pageable pageable = PageRequest.of(0, 3);
        List<BookBriefResponseIncludePublishDate> expect = List.of(appleBook, bananaBook, grapeBook);
        when(categoryRepository.getBooksForCategoryView(anyInt(), any())).thenReturn(new PageImpl<>(
                expect,
                pageable,
                10
        ));

        Page<BookBriefResponseIncludePublishDate> actual = categoryService.getBooksForCategoryView(1, pageable);

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).isNotNull().hasSize(expect.size());
        assertThat(actual.getContent().get(0).getId()).isNotNull().isEqualTo(appleBook.getId());
        assertThat(actual.getContent().get(1).getId()).isNotNull().isEqualTo(bananaBook.getId());
        assertThat(actual.getContent().get(2).getId()).isNotNull().isEqualTo(grapeBook.getId());
        assertThat(actual.getTotalPages()).isEqualTo(10 / 3 + 1);
        assertThat(actual.getTotalElements()).isEqualTo(10L);
    }

    @Test
    @DisplayName("getCategoriesForCategoryView")
    void givenCategoryId_whenGetCategoriesForCategoryView_thenReturnCategoryGetResponseForCategoryView() {
        Integer categoryId = 3;
        Category it = new Category(
                1,
                null,
                null,
                "IT",
                LocalDate.now());
        Category database = new Category(
                categoryId,
                it,
                null,
                "데이터베이스",
                LocalDate.now()
        );
        Category network = new Category(
                4,
                it,
                null,
                "네트워크",
                LocalDate.now()
        );
        Category mysql = new Category(
                10,
                database,
                null,
                "MySQL",
                LocalDate.now()
        );
        CategoryGetResponse itGetResponse = makeCategoryGetResponse(it.getId(), null, it.getName());
        CategoryGetResponse databaseGetResponse = makeCategoryGetResponse(
                database.getId(),
                itGetResponse,
                database.getName());
        CategoryGetResponse networkGetResponse = makeCategoryGetResponse(
                network.getId(),
                itGetResponse,
                itGetResponse.getName());
        List<CategoryGetResponse> levelTwoCategories = List.of(databaseGetResponse, networkGetResponse);

        CategoryGetResponse mysqlGetResponse = makeCategoryGetResponse(
                mysql.getId(),
                databaseGetResponse,
                mysql.getName());
        List<CategoryGetResponse> targetCategories = List.of(mysqlGetResponse);

        when(categoryRepository.existsById(anyInt())).thenReturn(true);
        when(categoryRepository.findHighestCategoryId(anyInt())).thenReturn(it.getId());
        when(categoryRepository.findCategoryById(it.getId())).thenReturn(
                new CategoryIdNameGetResponse(it.getId(), it.getName()));
        when(categoryRepository.findCategoryById(categoryId)).thenReturn(
                new CategoryIdNameGetResponse(database.getId(), database.getName()));
        when(categoryRepository.findAllByParentCategory_Id(it.getId())).thenReturn(levelTwoCategories);
        when(categoryRepository.findAllByParentCategory_Id(categoryId)).thenReturn(targetCategories);

        CategoryGetResponseForCategoryView actual = categoryService.getCategoriesForCategoryView(categoryId);

        assertThat(actual).isNotNull();
        assertThat(actual.getHighestCategoryName()).isEqualTo(it.getName());
        assertThat(actual.getName()).isEqualTo(database.getName());
        assertThat(actual.getLevelTwoCategories()).isNotNull().hasSize(levelTwoCategories.size());
        assertThat(actual.getLevelTwoCategories().get(0).getId()).isEqualTo(database.getId());
        assertThat(actual.getLevelTwoCategories().get(1).getId()).isEqualTo(network.getId());
        assertThat(actual.getTargetCategories()).isNotNull().hasSize(targetCategories.size());
        assertThat(actual.getTargetCategories().get(0).getId()).isEqualTo(mysql.getId());
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
        verify(categoryRepository, times(1)).save(any());
        verify(categoryMapper, times(1)).createResponse(any());
    }

    @Test
    @DisplayName("createCategory 메서드 중복된 CategoryName 의 경우")
    void givenCreateCategory_whenDuplicateName_thenThrowCategoryNameAlreadyExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(true);
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(null, "name");

        assertThrows(CategoryNameAlreadyExistsException.class,
                () -> categoryService.createCategory(categoryCreateRequest));
        verify(categoryRepository, times(1)).existsByName("name");
    }

    @Test
    @DisplayName("createCategory 존재하지 않는 parentCategoryId 를 넘겨준 경우")
    void givenNotExistsParentCategoryId_whenCreateCategory_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(1, "categoryName");

        assertThrows(CategoryNotExistsException.class,
                () -> categoryService.createCategory(categoryCreateRequest));
        verify(categoryRepository, times(1)).existsByName("categoryName");
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("modifyCategory 메서드 정상적인 경우")
    void givenModifyCategory_whenNormalCase_thenReturnCategoryModifyResponse() {
        Category parentCategory = new Category(1, null, null, "parentCategory", LocalDate.now());
        Category childCategory = new Category(2, null, null, "childCategory", LocalDate.now());
        CategoryModifyResponse expectedResponse = new CategoryModifyResponse();
        expectedResponse.setParentCategoryId(parentCategory.getId());
        expectedResponse.setParentCategoryName(childCategory.getName());

        when(categoryRepository.findById(childCategory.getId())).thenReturn(Optional.of(childCategory));
        when(categoryMapper.modifyResponse(any())).thenReturn(expectedResponse);

        CategoryModifyRequest categoryModifyRequest =
                new CategoryModifyRequest("newChildCategory");
        expectedResponse.setName(categoryModifyRequest.getName());
        CategoryModifyResponse actualResponse =
                categoryService.modifyCategory(childCategory.getId(), categoryModifyRequest);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getParentCategoryId()).isEqualTo(expectedResponse.getParentCategoryId());
        assertThat(actualResponse.getName()).isEqualTo(categoryModifyRequest.getName());
        verify(categoryRepository, times(1)).findById(childCategory.getId());
        verify(categoryMapper, times(1)).modifyResponse(any());
    }

    @Test
    @DisplayName("modifyCategory 메서드 중복된 CategoryName 의 경우")
    void givenModifyCategory_whenDuplicateName_thenThrowCategoryNameAlreadyExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(true);

        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest("duplicatedName");
        assertThrows(CategoryNameAlreadyExistsException.class,
                () -> categoryService.modifyCategory(1, categoryModifyRequest));
        verify(categoryRepository, times(1)).existsByName("duplicatedName");
    }

    @Test
    @DisplayName("modifyCategory 메서드 존재하지 않는 CategoryId 의 경우")
    void givenModifyCategory_whenNotExistsCategoryId_thenThrowCategoryNotExistsException() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        CategoryModifyRequest categoryModifyRequest = new CategoryModifyRequest("categoryName");
        assertThrows(CategoryNotExistsException.class, () -> categoryService.modifyCategory(1, categoryModifyRequest));
        verify(categoryRepository, times(1)).existsByName("categoryName");
        verify(categoryRepository, times(1)).findById(1);
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
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("deleteCategory 자식 카테고리가 있는 카테고리 지우는 경우")
    void givenCategoryIdThatHasChildCategory_whenDeleteCategory_thenThrowCannotDeleteParentCategoryException() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(new Category()));
        when(categoryRepository.countByParentCategory_Id(anyInt())).thenReturn(1);
        assertThrows(CannotDeleteParentCategoryException.class, () -> categoryService.deleteCategory(1));
        verify(categoryRepository, times(1)).findById(1);
        verify(categoryRepository, times(1)).countByParentCategory_Id(1);
    }

    private CategoryGetResponse makeCategoryGetResponse(Integer id, CategoryGetResponse parentCategoryGetResponse,
                                                        String name) {
        return new CategoryGetResponse() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public CategoryGetResponse getParentCategory() {
                return parentCategoryGetResponse;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}