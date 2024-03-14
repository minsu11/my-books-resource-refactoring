package store.mybooks.resource.bookauthor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.exception.AuthorNotExistException;
import store.mybooks.resource.author.repository.AuthorRepository;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookauthor.dto.request.BookAuthorCreateRequest;
import store.mybooks.resource.bookauthor.entity.BookAuthor;
import store.mybooks.resource.bookauthor.repository.BookAuthorRepository;

/**
 * packageName    : store.mybooks.resource.book_author.service <br/>
 * fileName       : BookAuthorService<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@Service
@RequiredArgsConstructor
public class BookAuthorService {
    private final BookAuthorRepository bookAuthorRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * methodName : createBookAuthor
     * author : newjaehun
     * description : BookId 와 Author Id 로 BookAuthor 추가.
     *
     * @param bookAuthorCreateRequest BookAuthorCreateRequest
     */
    @Transactional
    public void createBookAuthor(BookAuthorCreateRequest bookAuthorCreateRequest) {
        Long bookId = bookAuthorCreateRequest.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));

        for (Integer authorId : bookAuthorCreateRequest.getAuthorIdList()) {
            BookAuthor.Pk pk = new BookAuthor.Pk(
                    bookAuthorCreateRequest.getBookId(),
                    authorId);
            Author author =
                    authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistException(authorId));
            bookAuthorRepository.save(new BookAuthor(pk, book, author));
        }
    }

    /**
     * methodName : deleteBookAuthor
     * author : newjaehun
     * description : BookId 로 BookAuthor 에서 BookId에 대한 전체 저자 삭제.
     *
     * @param bookId Long
     */
    @Transactional
    public void deleteBookAuthor(Long bookId) {
        if (!bookAuthorRepository.existsByPk_BookId(bookId)) {
            throw new BookNotExistException(bookId);
        }
        bookAuthorRepository.deleteByPk_BookId(bookId);
    }
}
