package store.mybooks.resource.book_category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.book_category.service.BookCategoryService;

/**
 * packageName    : store.mybooks.resource.book_category.controller
 * fileName       : BookCategoryRestController
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-category")
public class BookCategoryRestController {
    private final BookCategoryService bookCategoryService;

    /**
     * methodName : createBookCategory <br>
     * author : damho-lee <br>
     * description : BookCategory 등록.<br>
     *
     * @param bookCategoryCreateRequest BookCategoryCreateRequest
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Void> createBookCategory(
            @RequestBody BookCategoryCreateRequest bookCategoryCreateRequest) {
        bookCategoryService.createBookCategory(bookCategoryCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED).build();
    }

    /**
     * methodName : deleteBookCategory <br>
     * author : damho-lee <br>
     * description : BookId 로 BookId 에 관한 데이터 모두 삭제.<br>
     *
     * @param bookId long
     * @return ResponseEntity
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookCategory(@PathVariable("bookId") Long bookId) {
        bookCategoryService.deleteBookCategory(bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
