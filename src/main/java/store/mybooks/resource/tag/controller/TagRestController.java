package store.mybooks.resource.tag.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.exception.TagValidationException;
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
     * methodName : getTag <br>
     * author : damho-lee <br>
     * description : id 로 태그 조회.<br>
     *
     * @param id Integer
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagGetResponse> getTag(@PathVariable("id") Integer id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagService.getTag(id));
    }

    /**
     * methodName : getTags
     * author : damho-lee
     * description : 모든 Tag 들을 TagGetResponse 로 변환하여 반환하는 메서드.
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<Page<TagGetResponse>> getTags(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagService.getTags(pageable));
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
            throw new TagValidationException(bindingResult);
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
            throw new TagValidationException(bindingResult);
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
}
