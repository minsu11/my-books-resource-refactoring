package store.mybooks.resource.book_category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.book_category.entity.BookCategory;
import store.mybooks.resource.book_category.repository.BookCategoryRepository;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.category.repository.CategoryRepository;

/**
 * packageName    : store.mybooks.resource.book_category.service
 * fileName       : BookCategoryService
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
public class BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    /**
     * methodName : createBookCategory <br>
     * author : damho-lee <br>
     * description : BookId 와 CategoryId 로 BookCategory 관계 생성.<br>
     *
     * @param bookCategoryCreateRequest BookCategoryCreateRequest
     */
    @Transactional
    public void createBookCategory(BookCategoryCreateRequest bookCategoryCreateRequest) {
        Long bookId = bookCategoryCreateRequest.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));

        for (Integer categoryId : bookCategoryCreateRequest.getCategoryIdList()) {
            BookCategory.Pk pk = new BookCategory.Pk(
                    bookCategoryCreateRequest.getBookId(),
                    categoryId);
            Category category =
                    categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new CategoryNotExistsException(categoryId));
            bookCategoryRepository.save(new BookCategory(pk, book, category));
        }
    }

    /**
     * methodName : deleteBookCategory <br>
     * author : damho-lee <br>
     * description : BookId 로 BookCategory 관계 삭제.<br>
     *
     * @param bookId long
     */
    @Transactional
    public void deleteBookCategory(Long bookId) {
        if (!bookCategoryRepository.existsByPk_BookId(bookId)) {
            throw new BookNotExistException(bookId);
        }

        bookCategoryRepository.deleteByPk_BookId(bookId);
    }
}
