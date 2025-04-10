package store.mybooks.resource.author.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import store.mybooks.resource.author.dto.request.AuthorCreateRequest;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.service.AuthorService;
import store.mybooks.resource.error.Utils;

/**
 * packageName    : store.mybooks.resource.author.controller
 * fileName       : AuthorRestController
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {
    private final AuthorService authorService;

    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * methodName : getAllAuthors
     * author : newjaehun
     * description : 전체 저자 리스트 반환.
     *
     * @return responseEntity
     */
    @GetMapping
    public ResponseEntity<List<AuthorGetResponse>> getAllAuthors() {
        List<AuthorGetResponse> authors = authorService.getAllAuthors();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authors);
    }

    /**
     * methodName : getAllAuthors
     * author : newjaehun
     * description : 페이징된 전체 저자 리스트 반환.
     *
     * @param pageable pageable
     * @return ResponseEntity
     */
    @GetMapping("/page")
    public ResponseEntity<Page<AuthorGetResponse>> getPagedAuthors(Pageable pageable) {
        Page<AuthorGetResponse> authors = authorService.getPagedAuthors(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authors);
    }

    /**
     * methodName : getAuthor
     * author : newjaehun
     * description : 저자 ID로 저자 반환.
     *
     * @param authorId authorId
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorGetResponse> getAuthor(@PathVariable("id") Integer authorId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.getAuthor(authorId));
    }

    /**
     * methodName : createAuthor
     * author : newjaehun
     * description : 저자 추가.
     *
     * @param createRequest 추가할 name, content 포함
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<AuthorCreateResponse> createAuthor(
            @Valid @RequestBody AuthorCreateRequest createRequest, BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        AuthorCreateResponse createResponse = authorService.createAuthor(createRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createResponse);
    }

    /**
     * methodName : modifyAuthor
     * author : newjaehun
     * description : 저자 수정.
     *
     * @param authorId      수정하려는 author 의 id
     * @param modifyRequest 수정할 namem, content 포함
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthorModifyResponse> modifyAuthor(@PathVariable("id") Integer authorId,
                                                             @Valid @RequestBody AuthorModifyRequest modifyRequest,
                                                             BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.modifyAuthor(authorId, modifyRequest));
    }

    /**
     * methodName : deleteAuthor
     * author : newjaehun
     * description : 저자 삭제.
     *
     * @param authorId 삭제하려는 author 의 id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Integer authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
