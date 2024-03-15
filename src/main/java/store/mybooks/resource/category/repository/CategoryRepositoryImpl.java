package store.mybooks.resource.category.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.dto.response.BookBriefResponseIncludePublishDate;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.bookcategory.entity.QBookCategory;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForQuerydsl;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.entity.QCategory;
import store.mybooks.resource.image.dto.response.ImageResponse;
import store.mybooks.resource.image.entity.QImage;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;

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
    QCategory category1 = new QCategory("category1");
    QCategory category2 = new QCategory("category2");
    QCategory category3 = new QCategory("category3");

    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public List<CategoryGetResponseForQuerydsl> findFullCategoryForBookViewByBookId(Long bookId) {
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

    @Override
    public Integer findHighestCategoryId(Integer categoryId) {
        Integer firstCategoryId = from(category1)
                .leftJoin(category2)
                .on(category1.parentCategory.id.eq(category2.id))
                .leftJoin(category3)
                .on(category2.parentCategory.id.eq(category3.id))
                .where(category1.id.eq(categoryId))
                .select(category3.id)
                .distinct()
                .fetchOne();

        Integer secondCategoryId = from(category1)
                .leftJoin(category2)
                .on(category1.parentCategory.id.eq(category2.id))
                .leftJoin(category3)
                .on(category2.parentCategory.id.eq(category3.id))
                .where(category1.id.eq(categoryId))
                .select(category2.id)
                .distinct()
                .fetchOne();

        return firstCategoryId == null ? secondCategoryId : firstCategoryId;
    }

    @Override
    public Page<BookBriefResponseIncludePublishDate> getBooksForCategoryView(Integer categoryId, Pageable pageable) {
        QBookCategory bookCategory = QBookCategory.bookCategory;
        QBook book = QBook.book;
        QImage image = QImage.image;

        List<Integer> childCategoryIds =
                from(category1)
                        .leftJoin(category2)
                        .on(category1.parentCategory.id.eq(category2.id))
                        .leftJoin(category3)
                        .on(category2.parentCategory.id.eq(category3.id))
                        .where(category1.id.eq(categoryId)
                                .or(category2.id.eq(categoryId))
                                .or(category3.id.eq(categoryId)))
                        .select(category1.id)
                        .distinct()
                        .fetch();

        List<BookBriefResponseIncludePublishDate> bookBriefResponseList =
                from(category1)
                        .leftJoin(bookCategory)
                        .on(category1.id.eq(bookCategory.category.id))
                        .leftJoin(book)
                        .on(book.id.eq(bookCategory.book.id))
                        .leftJoin(image)
                        .on(book.id.eq(image.book.id))
                        .where(book.bookStatus.id.in("판매중", "재고없음"))
                        .where(image.imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                        .where(bookCategory.category.id.in(childCategoryIds))
                        .orderBy(book.publishDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .select(Projections.constructor(BookBriefResponseIncludePublishDate.class,
                                book.id,
                                Projections.constructor(ImageResponse.class,
                                        image.path,
                                        image.fileName,
                                        image.extension),
                                book.name,
                                book.originalCost,
                                book.saleCost,
                                book.publishDate))
                        .distinct()
                        .fetch();

        long total = from(book)
                .leftJoin(bookCategory)
                .on(book.id.eq(bookCategory.book.id))
                .leftJoin(image)
                .on(book.id.eq(image.book.id))
                .where(book.bookStatus.id.in("판매중", "재고없음"))
                .where(image.imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                .where(bookCategory.category.id.in(childCategoryIds))
                .fetchCount();

        return new PageImpl<>(bookBriefResponseList, pageable, total);
    }
}
