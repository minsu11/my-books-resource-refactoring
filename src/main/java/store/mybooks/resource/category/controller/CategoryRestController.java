package store.mybooks.resource.category.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
 * fileName       : CategoryRestController
 * author         : damho
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    /**
     * methodName : getCategories <br>
     * author : damho-lee <br>
     * description : ParentCategoryId 를 기준으로 Caetgory 를 오름차수으로 반환. null 인 값이 가장 먼저 반환. 즉, 최상위 카테고리부터 반환됨. <br>
     *
     * @param pageable pagination. (default: page = 0, size = 10)
     * @return ResponseEntity
     */
    @GetMapping("/page")
    public ResponseEntity<Page<CategoryGetResponseForView>> getCategoriesOrderByParentCategoryId(
            @PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoriesOrderByParentCategoryIdForAdminPage(pageable));
    }

    /**
     * methodName : getCategoriesForBookCreate <br>
     * author : damho-lee <br>
     * description : 도서 등록할 때 필요한 카테고리 리스트 반환.<br>
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<CategoryGetResponseForBookCreate>> getCategoriesForBookCreate() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoriesForBookCreate());
    }

    /**
     * 메서드 이름 : getHighestCategories .
     * 작성자 : damho-lee
     * 설명 : 최상위 카테고리 리스트 반환
     *
     * @return ResponseEntity
     */
    @GetMapping("/highest")
    public ResponseEntity<List<CategoryGetResponse>> getHighestCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getHighestCategories());
    }

    /**
     * methodName : getCategoryNameForBookView <br>
     * author : damho-lee <br>
     * description : bookId 로 CategoryName 들 찾기.<br>
     *
     * @param bookId Long
     * @return response entity
     */
    @GetMapping("/bookId/{bookId}")
    public ResponseEntity<List<CategoryIdNameGetResponse>> getCategoryNameForBookView(
            @PathVariable("bookId") Long bookId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoryNameForBookView(bookId));
    }

    /**
     * 메서드 이름 : getCategoriesByParentCategoryId .
     * 작성자 : damho-lee
     * 설명 : parent category id 값에 따른 카테고리 리스트 반환
     *
     * @return ResponseEntity
     */
    @GetMapping("/parentCategoryId/{id}")
    public ResponseEntity<List<CategoryGetResponse>> getCategoriesByParentCategoryId(
            @PathVariable("id") int parentCategoryId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoriesByParentCategoryId(parentCategoryId));
    }

    /**
     * methodName : getCategory <br>
     * author : damho-lee <br>
     * description : id 로 카테고리 검색.<br>
     *
     * @param id int
     * @return response entity
     */
    @GetMapping("/categoryId/{id}")
    public ResponseEntity<CategoryGetResponseForUpdate> getCategoryForUpdate(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoryForUpdate(id));
    }

    /**
     * 메서드 이름 : createPost
     * 작성자 : damho-lee
     * 설명 : 카테고리 등록.
     *
     * @param categoryCreateRequest .
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<CategoryCreateResponse> createCategory(
            @Valid @RequestBody CategoryCreateRequest categoryCreateRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CategoryValidationException(bindingResult);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(categoryCreateRequest));
    }

    /**
     * methodName : modifyCategory
     * author : damho-lee
     * description : category 를 수정.
     *
     * @param id                    수정하려는 category 의 id.
     * @param categoryModifyRequest ParentCategoryId, name 포함.
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryModifyResponse> modifyCategory(
            @PathVariable("id") int id,
            @Valid @RequestBody CategoryModifyRequest categoryModifyRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CategoryValidationException(bindingResult);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.modifyCategory(id, categoryModifyRequest));
    }

    /**
     * methodName : deleteCategory
     * author : damho-lee
     * description : id 를 통해 카테고리 삭제.
     *
     * @param id 삭제하려는 category 의 id.
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDeleteResponse> deleteCategory(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.deleteCategory(id));
    }
}
