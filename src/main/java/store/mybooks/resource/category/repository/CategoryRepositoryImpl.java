package store.mybooks.resource.category.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.bookcategory.entity.QBookCategory;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForQuerydsl;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.entity.QCategory;

/**
 * packageName    : store.mybooks.resource.category.repository
 * fileName       : CategoryRepositoryImpl
 * author         : damho-lee
 * date           : 3/3/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/3/24          damho-lee          최초 생성
 */
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CategoryRepositoryCustom {
    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public List<CategoryGetResponseForQuerydsl> findFullCategoryForBookViewByBookId(Long bookId) {
        QCategory category1 = new QCategory("category1");
        QCategory category2 = new QCategory("category2");
        QCategory category3 = new QCategory("category3");
        QBookCategory bookCategory = QBookCategory.bookCategory;

        return from(category3)
                .leftJoin(category2)
                .on(category3.parentCategory.id.eq(category2.id))
                .leftJoin(category1)
                .on(category2.parentCategory.id.eq(category1.id))
                .leftJoin(bookCategory)
                .on(category3.id.eq(bookCategory.category.id))
                .where(bookCategory.book.id.eq(bookId))
                .select(Projections.constructor(CategoryGetResponseForQuerydsl.class,
                        category3.id,
                        category1.name,
                        category2.name,
                        category3.name))
                .fetch();
    }
}
