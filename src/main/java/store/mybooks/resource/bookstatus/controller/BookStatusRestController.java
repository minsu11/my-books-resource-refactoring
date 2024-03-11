package store.mybooks.resource.bookstatus.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.bookstatus.dto.response.BookStatusGetResponse;
import store.mybooks.resource.bookstatus.service.BookStatusService;

/**
 * packageName    : store.mybooks.resource.book_status.controller
 * fileName       : BookStatusRestController
 * author         : newjaehun
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24        newjaehun       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books-statuses")
public class BookStatusRestController {
    private final BookStatusService bookStatusService;


    /**
     * methodName : getAllBookStatuses
     * author : newajaehun
     * description : 전체 도서 상태 리스트 반환.
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<BookStatusGetResponse>> getAllBookStatuses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookStatusService.getAllBookStatus());
    }
}
