package store.mybooks.resource.category.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
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
     * @return response entity
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
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<CategoryGetResponse>> getCategoriesByParentCategoryId(@PathVariable("id") Integer id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategoriesByParentCategoryId(id));
    }

    /**
     * 메서드 이름 : createPost .
     * 작성자 : damho-lee
     * 설명 :
     *
     * @param categoryCreateRequest .
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<CategoryCreateResponse> createCategory(
            @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(categoryCreateRequest));
    }


}
