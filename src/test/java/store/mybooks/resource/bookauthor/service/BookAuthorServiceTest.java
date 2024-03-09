package store.mybooks.resource.bookauthor.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.repository.AuthorRepository;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookauthor.dto.request.BookAuthorCreateRequest;
import store.mybooks.resource.bookauthor.repository.BookAuthorRepository;

/**
 * packageName    : store.mybooks.resource.book_author.service <br/>
 * fileName       : BookAuthorServiceTest<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@ExtendWith(MockitoExtension.class)
class BookAuthorServiceTest {
    @Mock
    BookAuthorRepository bookAuthorRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookAuthorService bookAuthorService;

    private final Long bookId = 1L;

    @Test
    @DisplayName("BookAuthor 추가")
    void givenBookAuthorCreateRequest_whenCreatBookAuthor_thenCreateBookAuthor() {
        Author author1 = new Author(1, null, null, LocalDate.now());
        Author author2 = new Author(2, null, null, LocalDate.now());
        Author author3 = new Author(3, null, null, LocalDate.now());

        Book book = Book.builder()
                .id(bookId)
                .build();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(authorRepository.findById(1)).thenReturn(Optional.of(author1));
        when(authorRepository.findById(2)).thenReturn(Optional.of(author2));
        when(authorRepository.findById(3)).thenReturn(Optional.of(author3));
        when(bookAuthorRepository.save(any())).thenReturn(null);

        BookAuthorCreateRequest request = new BookAuthorCreateRequest(bookId, new ArrayList<>(List.of(1, 2, 3)));


        bookAuthorService.createBookAuthor(request);
        verify(bookRepository, times(1)).findById(anyLong());
        verify(authorRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).findById(2);
        verify(authorRepository, times(1)).findById(3);
        verify(bookAuthorRepository, times(3)).save(any());
    }

    @Test
    @DisplayName("도서저자 삭제(존재하는 BookID)")
    void givenBookId_whenDeleteBookAuthor_thenDeleteBookAuthor() {
        when(bookAuthorRepository.existsByPk_BookId(bookId)).thenReturn(true);
        doNothing().when(bookAuthorRepository).deleteByPk_BookId(anyLong());
        bookAuthorService.deleteBookAuthor(bookId);

        verify(bookAuthorRepository, times(1)).deleteByPk_BookId(bookId);

    }

    @Test
    @DisplayName("도서저자 삭제(존재하지 않는 BookID)")
    void givenNotExistsBookId_whenDeleteBookAuthor_thenThrowBookNotExistsException() {
        when(bookAuthorRepository.existsByPk_BookId(bookId)).thenReturn(false);
        assertThrows(BookNotExistException.class, () -> bookAuthorService.deleteBookAuthor(bookId));

        verify(bookAuthorRepository, times(0)).deleteByPk_BookId(bookId);
    }
}