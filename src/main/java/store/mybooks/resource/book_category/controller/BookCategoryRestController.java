//package store.mybooks.resource.book_category.controller;
//
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
//import store.mybooks.resource.book_category.dto.response.BookCategoryCreateResponse;
//import store.mybooks.resource.book_category.dto.response.BookGetResponse;
//import store.mybooks.resource.book_category.dto.response.CategoryGetResponse;
//import store.mybooks.resource.book_category.service.BookCategoryService;
//
///**
// * packageName    : store.mybooks.resource.book_category.controller
// * fileName       : BookCategoryRestController
// * author         : damho-lee
// * date           : 2/22/24
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2/22/24          damho-lee          최초 생성
// */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/book-category")
//public class BookCategoryRestController {
//    private final BookCategoryService bookCategoryService;
//
//    @GetMapping
//    public ResponseEntity<List<CategoryGetResponse>> getCategoryListByBookId(
//            @RequestParam(value = "bookId") Long bookId) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(bookCategoryService.getCategoryListByBookId(bookId));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BookGetResponse>> getBookListByCategoryId(
//            @RequestParam("categoryId") Integer categoryId) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(bookCategoryService.getBookListByCategoryId(categoryId));
//    }
//
//    @PostMapping
//    public ResponseEntity<BookCategoryCreateResponse> createBookCategory(
//            @RequestBody BookCategoryCreateRequest bookCategoryCreateRequest) {
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(bookCategoryService.createBookCategory(bookCategoryCreateRequest));
//    }
//}
