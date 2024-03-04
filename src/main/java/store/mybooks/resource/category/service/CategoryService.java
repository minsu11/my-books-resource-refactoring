package store.mybooks.resource.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.request.CategoryModifyRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForBookCreate;
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
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * methodName : getCategoriesOrderByParentCategoryId <br>
     * author : damho-lee <br>
     * description : ParentCategoryId 를 기준으로 Caetgory 를 오름차순으로 반환. null 인 값이 가장 먼저 반환. 즉, 최상위 카테고리부터 반환됨.<br>
     *
     * @param pageable pagination. (default: page = 0, size = 10)
     * @return list
     */
    @Transactional(readOnly = true)
    public Page<CategoryGetResponse> getCategoriesOrderByParentCategoryId(Pageable pageable) {
        return categoryRepository.findByOrderByParentCategory_Id(pageable);
    }

    /**
     * methodName : getCategoriesOrderByParentCategoryIdForAdminPage <br>
     * author : damho-lee <br>
     * description : 관리자 페이지의 카테고리 페이지에서 Pagination 을 위한 메서드. 1단계 카테고리가 IT 고,
     * 2단계 카테고리가 네트워크 인 경우 IT/네트워크 형식처럼 나오게 하기 위한 <br>
     *
     * @param pageable Pageable
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<CategoryGetResponseForView> getCategoriesOrderByParentCategoryIdForAdminPage(Pageable pageable) {
        Page<CategoryGetResponse> categoryGetResponsePage = categoryRepository.findByOrderByParentCategory_Id(pageable);
        List<CategoryGetResponseForView> categoryGetResponseForViewList = new ArrayList<>();

        for (CategoryGetResponse categoryGetResponse : categoryGetResponsePage.getContent()) {
            CategoryGetResponse secondCategory = categoryGetResponse.getParentCategory();
            CategoryGetResponse firstCategory =
                    secondCategory != null ? secondCategory.getParentCategory() : null;

            String secondCategoryName = secondCategory != null ? secondCategory.getName() : "";
            String firstCategoryName = firstCategory != null ? firstCategory.getName() : "";

            if (!firstCategoryName.isEmpty()) {
                secondCategoryName = firstCategoryName + "/" + secondCategoryName;
            }

            categoryGetResponseForViewList.add(new CategoryGetResponseForView(
                    categoryGetResponse.getId(),
                    categoryGetResponse.getName(),
                    secondCategoryName
            ));
        }

        return new PageImpl<>(categoryGetResponseForViewList, pageable, categoryGetResponsePage.getTotalElements());
    }

    /**
     * methodName : getCategoriesForBookCreate <br>
     * author : damho-lee <br>
     * description : 도서 등록할 때 카테고리 리스트를 보여주기 위한 함수.<br>
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<CategoryGetResponseForBookCreate> getCategoriesForBookCreate() {
        List<CategoryGetResponse> categoryGetResponseList =
                categoryRepository.findAllByOrderByParentCategory_Id().stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        List<CategoryGetResponseForBookCreate> categoryGetResponseForBookCreateList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (CategoryGetResponse categoryGetResponse : categoryGetResponseList) {
            CategoryGetResponse currentCategoryGetResponse = categoryGetResponse;

            if (categoryGetResponse.getId() == null) {
                continue;
            }

            while (currentCategoryGetResponse != null) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.insert(0, "/");
                }
                stringBuilder.insert(0, currentCategoryGetResponse.getName());
                currentCategoryGetResponse = currentCategoryGetResponse.getParentCategory();
            }

            categoryGetResponseForBookCreateList.add(new CategoryGetResponseForBookCreate(
                    categoryGetResponse.getId(),
                    stringBuilder.toString()
            ));
            stringBuilder.setLength(0);
        }

        return categoryGetResponseForBookCreateList;
    }

    /**
     * methodName : getCategoryNameForBookView <br>
     * author : damho-lee <br>
     * description : bookId 로 CategoryName 들 찾기.<br>
     *
     * @param bookId long
     * @return list
     */
    @Transactional(readOnly = true)
    public List<CategoryIdNameGetResponse> getCategoryNameForBookView(Long bookId) {
        List<CategoryGetResponseForQuerydsl> categoryNameGetResponseList =
                categoryRepository.findFullCategoryForBookViewByBookId(bookId);
        List<CategoryIdNameGetResponse> categoryNameList = new ArrayList<>();

        for (CategoryGetResponseForQuerydsl categoryGetResponseForQuerydsl : categoryNameGetResponseList) {
            StringJoiner stringJoiner = new StringJoiner("/");

            if (categoryGetResponseForQuerydsl.getName1() != null) {
                stringJoiner.add(categoryGetResponseForQuerydsl.getName1());
            }

            if (categoryGetResponseForQuerydsl.getName2() != null) {
                stringJoiner.add(categoryGetResponseForQuerydsl.getName2());
            }

            if (categoryGetResponseForQuerydsl.getName3() != null) {
                stringJoiner.add(categoryGetResponseForQuerydsl.getName3());
            }

            categoryNameList.add(new CategoryIdNameGetResponse() {
                @Override
                public Integer getId() {
                    return categoryGetResponseForQuerydsl.getId();
                }

                @Override
                public String getName() {
                    return stringJoiner.toString();
                }
            });
        }

        return categoryNameList;
    }

    /**
     * methodName : getCategory <br>
     * author : damho-lee <br>
     * description : id 로 Category 검색.<br>
     *
     * @param id int
     * @return CategoryGetResponse
     */
    @Transactional(readOnly = true)
    public CategoryGetResponseForUpdate getCategoryForUpdate(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotExistsException(id);
        }

        CategoryGetResponse categoryGetResponse = categoryRepository.queryById(id);

        CategoryGetResponse levelOneCategory = categoryGetResponse.getParentCategory();
        CategoryGetResponse levelTwoCategory = null;
        if (levelOneCategory != null && levelOneCategory.getParentCategory() != null) {
            levelTwoCategory = levelOneCategory;
            levelOneCategory = levelOneCategory.getParentCategory();
        }

        String levelOneCategoryName = levelOneCategory == null ? null : levelOneCategory.getName();
        String levelTwoCategoryName = levelTwoCategory == null ? null : levelTwoCategory.getName();

        return new CategoryGetResponseForUpdate(
                new CategoryIdNameGetResponse() {
                    @Override
                    public Integer getId() {
                        return categoryGetResponse.getId();
                    }

                    @Override
                    public String getName() {
                        return categoryGetResponse.getName();
                    }
                }, levelOneCategoryName, levelTwoCategoryName);
    }

    /**
     * methodName : getHighestCategories <br>
     * author : damho-lee <br>
     * description : 최상위 Category 들을 반환.<br>
     *
     * @return List
     */
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
     * @return List
     */
    @Transactional(readOnly = true)
    public List<CategoryGetResponse> getCategoriesByParentCategoryId(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotExistsException(id);
        }

        return categoryRepository.findAllByParentCategory_Id(id);
    }

    /**
     * methodName : createCategory
     * author : damho-lee
     * description : CategoryRequest 로 category 를 저장하는 메서드 카테고리 이름이 이미 존재하는 경우 CategoryNameAlreadyExistsException 발생.
     *
     * @param categoryCreateRequest CategoryCreateRequest
     * @return CategoryCreateRequest
     */
    public CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        if (categoryRepository.existsByName(categoryCreateRequest.getName())) {
            throw new CategoryNameAlreadyExistsException(categoryCreateRequest.getName());
        }

        Category parentCategory = null;
        Integer parentCategoryId = categoryCreateRequest.getParentCategoryId();
        String name = categoryCreateRequest.getName();

        if (parentCategoryId != null) {
            parentCategory = categoryRepository.findById(parentCategoryId)
                    .orElseThrow(() -> new CategoryNotExistsException(parentCategoryId));
        }

        return categoryMapper.createResponse(categoryRepository.save(new Category(parentCategory, name)));
    }

    /**
     * methodName : modifyCategory
     * author : damho-lee
     * description : category 수정.
     *
     * @param id                    수정하려는 category 의 id. 존재하지 않으면 CategoryNotExistsException.
     * @param categoryModifyRequest CategoryModifyRequest
     * @return CategoryModifyRequest
     */
    public CategoryModifyResponse modifyCategory(int id, CategoryModifyRequest categoryModifyRequest) {
        if (categoryRepository.existsByName(categoryModifyRequest.getName())) {
            throw new CategoryNameAlreadyExistsException(categoryModifyRequest.getName());
        }

        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotExistsException(id));

        return categoryMapper.modifyResponse(
                category.modifyCategory(categoryModifyRequest.getName()));
    }

    /**
     * methodName : deleteCategory
     * author : damho-lee
     * description : id 를 통해 category 삭제.
     *
     * @param id 삭제하고자 하는 카테고리의 id. id 에 해당하는 category 가 없는 경우 CategoryNotExistsException.
     * @return CategoryDeleteResponse
     */
    public CategoryDeleteResponse deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotExistsException(id));
        int count = categoryRepository.countByParentCategory_Id(id);

        if (count > 0) {
            throw new CannotDeleteParentCategoryException();
        }

        categoryRepository.deleteById(id);

        return categoryMapper.deleteResponse(category);
    }
}
