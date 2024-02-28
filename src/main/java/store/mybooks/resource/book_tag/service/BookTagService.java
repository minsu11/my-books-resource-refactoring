package store.mybooks.resource.book_tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.book_tag.repository.BookTagRepository;

/**
 * packageName    : store.mybooks.resource.book_tag.service
 * fileName       : BookTagService
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@Service
@RequiredArgsConstructor
public class BookTagService {
    private final BookTagRepository bookTagRepository;
}
