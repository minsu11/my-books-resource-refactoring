package store.mybooks.resource.book_category.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.book_category.dto.response.BookGetResponse;
import store.mybooks.resource.book_category.dto.response.CategoryGetResponse;
import store.mybooks.resource.book_category.entity.QBookCategory;
import store.mybooks.resource.category.entity.QCategory;

/**
 * packageName    : store.mybooks.resource.book_category.repository
 * fileName       : BookCategoryRepositoryImpl
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
public class BookCategoryRepositoryImpl extends QuerydslRepositorySupport implements BookCategoryRepositoryCustom {
    public BookCategoryRepositoryImpl() {
        super(CategoryGetResponse.class);
    }

    @Override
    public List<CategoryGetResponse> getCategoryListByBookId(long bookId) {
        QCategory category = QCategory.category;
        QBookCategory bookCategory = QBookCategory.bookCategory;

        return from(bookCategory)
                .leftJoin(bookCategory.category, category)
                .on(bookCategory.category.id.eq(category.id))
                .where(bookCategory.book.id.eq(bookId))
                .select(Projections.constructor(CategoryGetResponse.class,
                        category.parentCategory.id,
                        category.name))
                .fetch();
    }

    @Override
    public List<BookGetResponse> getBookListByCategoryId(int categoryId) {
        QBook book = QBook.book;
        QBookCategory bookCategory = QBookCategory.bookCategory;

        return from(bookCategory)
                .leftJoin(bookCategory.book, book)
                .on(bookCategory.book.id.eq(book.id))
                .where(bookCategory.category.id.eq(categoryId))
                .select(Projections.constructor(BookGetResponse.class))
                .fetch();
    }
}
