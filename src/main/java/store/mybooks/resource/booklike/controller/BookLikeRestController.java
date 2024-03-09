package store.mybooks.resource.booklike.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.booklike.service.BookLikeService;
import store.mybooks.resource.config.HeaderProperties;

/**
 * packageName    : store.mybooks.resource.book_like.controller <br/>
 * fileName       : BookLikeRestController<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-likes")
public class BookLikeRestController {
    private final BookLikeService bookLikeService;

    /**
     * methodName : getUserBookLike
     * author : newjaehun
     * description : 사용자가 좋아요 누른 도서에 대한 간단한 정보.
     *
     * @param userId   Long
     * @param pageable Pageable
     * @return responseEntity
     */
    @GetMapping
    public ResponseEntity<Page<BookBriefResponse>> getUserBookLike(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId, @PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookLikeService.getUserBookLike(userId, pageable));
    }

    /**
     * methodName : updateUserBookLike
     * author : newjaehun
     * description : 사용자가 도서좋아요 및 취소 기능.
     *
     * @param userId Long
     * @param bookId Long
     * @return response entity
     */
    @PostMapping("/{bookId}")
    public ResponseEntity<Boolean> updateUserBookLike(@RequestHeader(name = HeaderProperties.USER_ID) Long userId,
                                                      @PathVariable("bookId") Long bookId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookLikeService.updateUserBookLike(userId, bookId));
    }
}
