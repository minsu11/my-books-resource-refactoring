package store.mybooks.resource.author.controller;

import javax.validation.Valid;
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
import store.mybooks.resource.author.dto.response.AuthorDeleteResponse;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.service.AuthorService;

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
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorGetResponse>> getAllAuthors(Pageable pageable) {
        Page<AuthorGetResponse> authors = authorService.getAllAuthors(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authors);
    }

    @PostMapping
    public ResponseEntity<AuthorCreateResponse> createAuthor(
            @Valid @RequestBody AuthorCreateRequest createRequest, BindingResult bindingResult) {
        AuthorCreateResponse createResponse = authorService.createAuthor(createRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorModifyResponse> modifyAuthor(@PathVariable("id") Integer authorId, @Valid @RequestBody
    AuthorModifyRequest modifyRequest, BindingResult bindingResult) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.modifyAuthor(authorId, modifyRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDeleteResponse> deleteAuthor(@PathVariable("id") Integer authorId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.deleteAuthor(authorId));
    }
}
