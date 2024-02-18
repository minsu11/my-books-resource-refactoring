package store.mybooks.resource.tag.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TagCreateResponse> createTag(@RequestBody TagCreateRequest tagCreateRequest) {
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
                                                       @RequestBody TagModifyRequest tagModifyRequest) {
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
