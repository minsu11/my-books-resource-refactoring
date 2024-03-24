package store.mybooks.resource.elastic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.elastic.repository.ElasticRepository;

/**
 * packageName    : store.mybooks.resource.elastic.service <br/>
 * fileName       : ElasticService<br/>
 * author         : newjaehun <br/>
 * date           : 3/19/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/19/24        newjaehun       최초 생성<br/>
 */
@Service
@RequiredArgsConstructor
public class ElasticService {
    private final ElasticRepository elasticRepository;

    /**
     * methodName : search
     * author : newjaehun
     * description : 검색 서비스 메소드.
     *
     * @param query    String
     * @param pageable Pageable
     * @return page
     */
    public Page<BookBriefResponse> search(String query, Pageable pageable) {
        return elasticRepository.search(query, pageable);
    }
}
