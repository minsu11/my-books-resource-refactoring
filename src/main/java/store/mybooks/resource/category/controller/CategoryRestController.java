package store.mybooks.resource.category.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
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
     * 메서드 이름 : getHighestCategories .
     * 작성자 : damho-lee
     * 설명 : 최상위 카테고리 리스트 반환
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<CategoryGetResponse>> getHighestCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getHighestCategories());
    }

    /**
     * 메서드 이름 : getCategoriesByParentCategoryId .
     * 작성자 : damho-lee
     * 설명 : parent category id 값에 따른 카테고리 리스트 반환
     *
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<CategoryGetResponse>> getCategoriesByParentCategoryId(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoriesByParentCategoryId(id));
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
            @RequestBody CategoryCreateRequest categoryCreateRequest) {
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
            @RequestBody CategoryModifyRequest categoryModifyRequest) {
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
