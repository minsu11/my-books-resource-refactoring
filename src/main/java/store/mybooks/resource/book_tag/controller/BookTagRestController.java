//package store.mybooks.resource.book_tag.controller;
//
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import store.mybooks.resource.book_category.dto.response.BookGetResponse;
//import store.mybooks.resource.book_tag.service.BookTagService;
//
///**
// * packageName    : store.mybooks.resource.book_tag.controller
// * fileName       : BookTagRestController
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
//@RequestMapping("/api/book-tag")
//public class BookTagRestController {
//    private final BookTagService bookTagService;
//
//    @GetMapping
//    public ResponseEntity<List<BookGetResponse>> getBookByTagId(@RequestParam("tagId") Integer tagId) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(bookTagService.getBookByTagId(tagId));
//    }
//}
