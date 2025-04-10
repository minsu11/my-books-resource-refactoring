package store.mybooks.resource.publisher.controller;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.error.Utils;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.service.PublisherService;

/**
 * packageName    : store.mybooks.resource.publisher.controller
 * fileName       : PublisherRestController
 * author         : newjaehun
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        newjaehun       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherRestController {
    private final PublisherService publisherService;

    /**
     * methodName : getAllPublishers
     * author : newjaehun
     * description : 전체 출판사 리스트 반환.
     *
     * @return responseEntity
     */
    @GetMapping
    public ResponseEntity<List<PublisherGetResponse>> getAllPublishers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(publisherService.getAllPublishers());
    }


    /**
     * methodName : getAllPublishers
     * author : newjaehun
     * description : 페이징된 전체 출판사 리스트 반환.
     *
     * @param pageable pageable
     * @return ResponseEntity
     */
    @GetMapping("/page")
    public ResponseEntity<Page<PublisherGetResponse>> getPagedPublisher(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(publisherService.getPagedPublisher(pageable));
    }


    /**
     * methodName : createPublisher
     * author : newjaehun
     * description : 출판사 추가.
     *
     * @param createRequest 추가할 name 포함
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<PublisherCreateResponse> createPublisher(
            @Valid @RequestBody PublisherCreateRequest createRequest, BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publisherService.createPublisher(createRequest));
    }

    /**
     * methodName : modifyPublisher
     * author : newjaehun
     * description : 출판사 수정.
     *
     * @param publisherId   수정하려는 publisher 의 id
     * @param modifyRequest 수정할 name 포함
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublisherModifyResponse> modifyPublisher(@PathVariable("id") Integer publisherId,
                                                                   @Valid @RequestBody
                                                                   PublisherModifyRequest modifyRequest,
                                                                   BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(publisherService.modifyPublisher(publisherId, modifyRequest));
    }


    /**
     * methodName : deletePublisher
     * author : newjaehun
     * description : 출판사 삭제.
     *
     * @param publisherId 삭제하려는 publisher 의 id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable("id") Integer publisherId) {
        publisherService.deletePublisher(publisherId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
