package store.mybooks.resource.book_author.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.book_author.dto.request.BookAuthorCreateRequest;
import store.mybooks.resource.book_author.service.BookAuthorService;

/**
 * packageName    : store.mybooks.resource.book_author.controller <br/>
 * fileName       : BookAuthorRestController<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-author")
public class BookAuthorRestController {
    private final BookAuthorService bookAuthorService;

    /**
     * methodName : createBookAuthor
     * author : newjaehun
     * description : BookAuthor 추가.
     *
     * @param bookAuthorCreateRequest BookAuthorCreateRequest
     * @return responseEntity
     */
    @PostMapping
    public ResponseEntity<Void> createBookAuthor(@Valid @RequestBody BookAuthorCreateRequest bookAuthorCreateRequest,
                                                 BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        bookAuthorService.createBookAuthor(bookAuthorCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    /**
     * methodName : deleteBookAuthor
     * author : newjaehun
     * description : BookId 로 BookAuthor 전체 삭제.
     *
     * @param bookId Long
     * @return responseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookAuthor(@PathVariable("id") Long bookId) {
        bookAuthorService.deleteBookAuthor(bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
