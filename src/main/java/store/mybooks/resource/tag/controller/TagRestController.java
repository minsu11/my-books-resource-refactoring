package store.mybooks.resource.tag.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.exception.TagNameAlreadyExistsException;
import store.mybooks.resource.tag.exception.TagNotExistsException;
import store.mybooks.resource.tag.service.TagService;

/**
 * packageName    : store.mybooks.resource.tag.controller
 * fileName       : TagRestController
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagRestController {
    private final TagService tagService;

    /**
     * methodName : getTags
     * author : damho-lee
     * description : 모든 Tag 들을 TagGetResponse 로 변환하여 반환하는 메서드.
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<TagGetResponse>> getTags() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagService.getTags());
    }

    /**
     * methodName : createTag
     * author : damho-lee
     * description : TagCreateRequest 를 통해 Tag 를 생성하는 메서드.
     *
     * @param tagCreateRequest name 을 포함.
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<TagCreateResponse> createTag(@Valid @RequestBody TagCreateRequest tagCreateRequest,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Tag 이름은 1글자 이상 10글자 이하여야 합니다.");
        }
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagService.createTag(tagCreateRequest));
    }

    /**
     * methodName : modifyTag
     * author : damho-lee
     * description : tag 수정하는 메서드.
     *
     * @param id               수정하려는 tag 의 id.
     * @param tagModifyRequest name 을 포함.
     * @return response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagModifyResponse> modifyTag(@PathVariable("id") int id,
                                                       @Valid @RequestBody TagModifyRequest tagModifyRequest,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Tag 이름은 1글자 이상 10글자 이하여야 합니다.");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagService.modifyTag(id, tagModifyRequest));
    }

    /**
     * methodName : deleteTag
     * author : damho-lee
     * description : 태그 삭제 메서드.
     *
     * @param id 삭제하려는 Tag 의 id.
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<TagDeleteResponse> deleteTag(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagService.deleteTag(id));
    }

    /**
     * methodName : tagNotExistsExceptionHandler
     * author : damho-lee
     * description : TagNotExistsException 를 처리하는 ExceptionHandler.
     *
     * @param exception TagNotExistsException.
     * @return ResponseEntity, 404 에러
     */
    @ExceptionHandler(TagNotExistsException.class)
    public ResponseEntity<String> tagNotExistsExceptionHandler(TagNotExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    /**
     * methodName : tagNameAlreadyExistsExceptionHandler
     * author : damho-lee
     * description : TagNameAlreadyExistsException 를 처리하는 Exception Handler.
     *
     * @param exception TagNameAlreadyExistsException
     * @return ResponseEntity, 404 에러
     */
    @ExceptionHandler(TagNameAlreadyExistsException.class)
    public ResponseEntity<String> tagNameAlreadyExistsExceptionHandler(TagNameAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
