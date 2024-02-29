package store.mybooks.resource.book_tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.book_tag.dto.request.BookTagCreateRequest;
import store.mybooks.resource.book_tag.entity.BookTag;
import store.mybooks.resource.book_tag.repository.BookTagRepository;
import store.mybooks.resource.tag.entity.Tag;
import store.mybooks.resource.tag.exception.TagNotExistsException;
import store.mybooks.resource.tag.repository.TagRepository;

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
@Transactional
public class BookTagService {
    private final BookTagRepository bookTagRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    /**
     * methodName : createBookTag <br>
     * author : damho-lee <br>
     * description : BookTag 등록.<br>
     *
     * @param bookTagCreateRequest BookTagCreateRequest
     */
    public void createBookTag(BookTagCreateRequest bookTagCreateRequest) {
        Long bookId = bookTagCreateRequest.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));

        for (Integer tagId : bookTagCreateRequest.getTagIdList()) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotExistsException(tagId));
            BookTag.Pk pk = new BookTag.Pk(bookId, tagId);
            bookTagRepository.save(new BookTag(pk, book, tag));
        }
    }

    /**
     * methodName : deleteBookTag <br>
     * author : damho-lee <br>
     * description : BookId 로 BookTag 삭제.<br>
     *
     * @param bookId long
     */
    public void deleteBookTag(Long bookId) {
        if (!bookTagRepository.existsByPk_BookId(bookId)) {
            throw new BookNotExistException(bookId);
        }
        bookTagRepository.deleteByPk_BookId(bookId);
    }
}
