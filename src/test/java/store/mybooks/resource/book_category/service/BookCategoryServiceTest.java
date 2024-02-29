package store.mybooks.resource.book_category.service;

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
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.book_category.repository.BookCategoryRepository;
import store.mybooks.resource.book_status.entity.BookStatus;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.repository.CategoryRepository;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.book_category.service
 * fileName       : BookCategoryServiceTest
 * author         : damho-lee
 * date           : 2/29/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/29/24          damho-lee          최초 생성
 */
@ExtendWith(MockitoExtension.class)
class BookCategoryServiceTest {
    @Mock
    BookCategoryRepository bookCategoryRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    BookCategoryService bookCategoryService;

    @Test
    @DisplayName("createBookCategory 테스트")
    void givenBookCategoryCreateRequest_whenCreateBookCategory_thenCreateBookCategory() {
        List<Integer> categoryIdList = new ArrayList<>();
        categoryIdList.add(1);
        categoryIdList.add(2);
        categoryIdList.add(3);
        Category firstCategory = new Category(1, null, null, "firstCategory", LocalDate.now());
        Category secondCategory = new Category(1, null, null, "secondCategory", LocalDate.now());
        Category thirdCategory = new Category(1, null, null, "thirdCategory", LocalDate.now());
        Book book =
                new Book(1L, new BookStatus("판매중"), new Publisher(1, "출판사1", LocalDate.now()), "도서1", "1234567898764",
                        LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(firstCategory));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(secondCategory));
        when(categoryRepository.findById(3)).thenReturn(Optional.of(thirdCategory));
        when(bookCategoryRepository.save(any())).thenReturn(null);
        BookCategoryCreateRequest bookCategoryCreateRequest = new BookCategoryCreateRequest(1L, categoryIdList);

        bookCategoryService.createBookCategory(bookCategoryCreateRequest);

        verify(bookCategoryRepository, times(categoryIdList.size())).save(any());
    }

    @Test
    @DisplayName("deleteBookCategory 테스트")
    void givenBookId_whenDeleteBookCategory_thenDeleteBookCategory() {
        when(bookCategoryRepository.existsByPk_BookId(anyLong())).thenReturn(true);
        doNothing().when(bookCategoryRepository).deleteByPk_BookId(anyLong());
        bookCategoryService.deleteBookCategory(1L);
        verify(bookCategoryRepository, times(1)).deleteByPk_BookId(anyLong());
    }

    @Test
    @DisplayName("deleteBookCategory 존재하지 않는 BookId 테스트")
    void givenNotExistsBookId_whenDeleteBookCategory_thenThrowBookNotExistException() {
        when(bookCategoryRepository.existsByPk_BookId(anyLong())).thenReturn(false);
        assertThrows(BookNotExistException.class, () -> bookCategoryService.deleteBookCategory(1L));
    }
}