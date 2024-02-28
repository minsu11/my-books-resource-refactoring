package store.mybooks.resource.book_category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.book_category.dto.response.BookCategoryCreateResponse;
import store.mybooks.resource.book_category.dto.response.BookGetResponse;
import store.mybooks.resource.book_category.dto.response.CategoryGetResponse;
import store.mybooks.resource.book_category.repository.BookCategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public List<CategoryGetResponse> getCategoryListByBookId(Long bookId) {
        return bookCategoryRepository.getCategoryListByBookId(bookId);
    }

    public List<BookGetResponse> getBookListByCategoryId(Integer categoryId) {
        return bookCategoryRepository.getBookListByCategoryId(categoryId);
    }

    public BookCategoryCreateResponse createBookCategory(BookCategoryCreateRequest bookCategoryCreateRequest) {
        return null;
    }
}
