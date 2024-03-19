package store.mybooks.resource.elastic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.elastic.service.ElasticService;

/**
 * packageName    : store.mybooks.resource.elastic.controller <br/>
 * fileName       : ElasticRestController<br/>
 * author         : newjaehun <br/>
 * date           : 3/19/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/19/24        newjaehun       최초 생성<br/>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class ElasticRestController {
    private final ElasticService elasticService;

    /**
     * methodName : getSearchPage
     * author : newjaehun
     * description : 검색결과(정렬 포함)을 호출하는 메소드.
     *
     * @param query    String
     * @param pageable Pageable
     * @return responseEntity
     */
    @GetMapping
    public ResponseEntity<Page<BookBriefResponse>> getSearchPage(
            @RequestParam(value = "query", required = false) String query, Pageable pageable) {
        if (query != null) {
            return ResponseEntity.status(HttpStatus.OK).body(elasticService.search(query, pageable));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}