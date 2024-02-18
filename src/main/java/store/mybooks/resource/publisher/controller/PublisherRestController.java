package store.mybooks.resource.publisher.controller;

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
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
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
     * description : 전체 출판사 리스트 반환
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PublisherGetResponse>> getAllPublishers() {
        List<PublisherGetResponse> publishers = publisherService.getAllPublisher();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(publishers);
    }

    /**
     * methodName : createPublisher
     * author : newjaehun
     * description : 출판사 추가
     *
     * @param createRequest: 추가할 name 포함
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<PublisherCreateResponse> createPublisher(
            @RequestBody PublisherCreateRequest createRequest) {
        PublisherCreateResponse createPublisher = publisherService.createPublisher(createRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createPublisher);
    }

    /**
     * methodName : modifyPublisher
     * author : newjaehun
     * description : 출판사 수정
     *
     * @param publisherId: 수정하려는 publisher 의 id
     * @param modifyRequest: 수정할 name 포함
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublisherModifyResponse> modifyPublisher(@PathVariable("id") Integer publisherId, @RequestBody PublisherModifyRequest modifyRequest) {
        PublisherModifyResponse modifyPublisher = publisherService.modifyPublisher(publisherId, modifyRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifyPublisher);
    }


    /**
     * methodName : deletePublisher
     * author : newjaehun
     * description : 출판사 삭제
     *
     * @param publisherId: 수정하려는 publisher 의 id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PublisherDeleteResponse> deletePublisher(@PathVariable("id") Integer publisherId) {
        PublisherDeleteResponse deleteResponse = publisherService.deletePublisher(publisherId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deleteResponse);
    }
}
