package store.mybooks.resource.booklike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.booklike.entity.BookLike;
import store.mybooks.resource.booklike.repository.BookLikeRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

/**
 * packageName    : store.mybooks.resource.book_like.service <br/>
 * fileName       : BookLikeService<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
@Service
@RequiredArgsConstructor
public class BookLikeService {
    private final BookLikeRepository bookLikeRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * methodName : getUserBookLike
     * author : newjaehun
     * description : 사용자가 좋아요 누른 도서에 대한 간단한 정보.
     *
     * @param userId   Long
     * @param pageable Pageable
     * @return page
     */
    @Transactional(readOnly = true)
    public Page<BookBriefResponse> getUserBookLike(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
        return bookLikeRepository.getUserBookLike(userId, pageable);
    }

    /**
     * methodName : updateUserBookLike
     * author : newjaehun
     * description : 사용자가 도서좋아요 및 취소 기능.
     *
     * @param userId Long
     * @param bookId Long
     * @return boolean
     */
    @Transactional
    public Boolean updateUserBookLike(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));

        BookLike.Pk pk = new BookLike.Pk(userId, bookId);
        if (!bookLikeRepository.existsById(pk)) {
            bookLikeRepository.save(new BookLike(pk, user, book));
            return true;
        } else {
            bookLikeRepository.deleteById(pk);
            return false;
        }

    }

    /**
     * methodName : isUserBookLike
     * author : newjaehun
     * description : 사용자의 특정도서 좋아요 유무.
     *
     * @param userId Long
     * @param bookId Long
     * @return boolean
     */
    public Boolean isUserBookLike(Long userId, Long bookId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }
        BookLike.Pk pk = new BookLike.Pk(userId, bookId);
        return bookLikeRepository.existsById(pk);
    }
}
