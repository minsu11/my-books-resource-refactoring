package store.mybooks.resource.book_status.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book_status.dto.response.BookStatusGetResponse;
import store.mybooks.resource.book_status.respository.BookStatusRepository;

/**
 * packageName    : store.mybooks.resource.book_status.service
 * fileName       : BookStatusService
 * author         : newjaehun
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24        newjaehun       최초 생성
 */
@Service
@RequiredArgsConstructor
public class BookStatusService {
    private final BookStatusRepository bookStatusRepository;

    @Transactional(readOnly = true)
    public List<BookStatusGetResponse> getAllBookStatus() {
        return bookStatusRepository.findAllBy();
    }
}
