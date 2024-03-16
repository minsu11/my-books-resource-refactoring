package store.mybooks.resource.book.repotisory;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.entity.QAuthor;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookGetResponseForCoupon;
import store.mybooks.resource.book.dto.response.BookResponseForOrder;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.bookauthor.entity.QBookAuthor;
import store.mybooks.resource.bookstatus.entity.QBookStatus;
import store.mybooks.resource.booktag.entity.QBookTag;
import store.mybooks.resource.image.dto.response.ImageResponse;
import store.mybooks.resource.image.entity.QImage;
import store.mybooks.resource.image_status.entity.QImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.entity.QPublisher;
import store.mybooks.resource.tag.dto.response.TagGetResponseForBookDetail;
import store.mybooks.resource.tag.entity.QTag;

/**
 * packageName    : store.mybooks.resource.book.repotisory <br/>
 * fileName       : BookRepositoryImpl<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
public class BookRepositoryImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {
    /**
     * Instantiates a new Book repository.
     */
    public BookRepositoryImpl() {
        super(Book.class);
    }

    /**
     * The Book.
     */
    QBook book = QBook.book;
    /**
     * The Book status.
     */
    QBookStatus bookStatus = QBookStatus.bookStatus;
    /**
     * The Author.
     */
    QAuthor author = QAuthor.author;
    /**
     * The Publisher.
     */
    QPublisher publisher = QPublisher.publisher;
    /**
     * The Tag.
     */
    QTag tag = QTag.tag;
    /**
     * The Book author.
     */
    QBookAuthor bookAuthor = QBookAuthor.bookAuthor;
    /**
     * The Book tag.
     */
    QBookTag bookTag = QBookTag.bookTag;
    /**
     * The Image.
     */
    QImage image = QImage.image;
    /**
     * The Image status.
     */
    QImageStatus imageStatus = QImageStatus.imageStatus;

    @Override
    public BookDetailResponse getBookDetailInfo(Long id) {
        Book result = from(book)
                .join(book.bookStatus, bookStatus)
                .join(book.publisher, publisher)
                .where(book.id.eq(id))
                .fetchOne();

        ImageResponse thumbNailImage = from(image)
                .join(image.book, book)
                .join(image.imageStatus, imageStatus)
                .where(image.book.id.eq(id))
                .where(imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                .select(Projections.constructor(ImageResponse.class, image.path, image.fileName, image.extension))
                .fetchOne();

        List<ImageResponse> contentImageList = from(image)
                .join(image.book, book)
                .join(image.imageStatus, imageStatus)
                .where(image.book.id.eq(id))
                .where(imageStatus.id.eq(ImageStatusEnum.CONTENT.getName()))
                .select(Projections.constructor(ImageResponse.class, image.path, image.fileName, image.extension))
                .fetch();

        List<AuthorGetResponse> authorList = from(bookAuthor)
                .join(bookAuthor.author, author)
                .where(bookAuthor.book.id.eq(id))
                .select(Projections.constructor(AuthorGetResponse.class, author.id, author.name, author.content))
                .fetch();
        
        List<TagGetResponseForBookDetail> tagList = from(bookTag)
                .join(bookTag.tag, tag)
                .where(bookTag.book.id.eq(id))
                .select(Projections.constructor(TagGetResponseForBookDetail.class, tag.id, tag.name))
                .fetch();

        return BookDetailResponse.builder()
                .id(result.getId())
                .thumbNailImage(thumbNailImage)
                .name(result.getName())
                .bookStatus(result.getBookStatus().getId())
                .publisher(new PublisherGetResponse(result.getPublisher().getId(),
                        result.getPublisher().getName()))
                .publishDate(result.getPublishDate())
                .saleCost(result.getSaleCost())
                .originalCost(result.getOriginalCost())
                .disCountRate(result.getDiscountRate())
                .isPacking(result.getIsPackaging())
                .page(result.getPage())
                .isbn(result.getIsbn())
                .stock(result.getStock())
                .index(result.getIndex())
                .explanation(result.getExplanation())
                .contentImageList(contentImageList)
                .authorList(authorList)
                .tagList(tagList)
                .build();
    }

    @Override
    public Page<BookBriefResponse> getBookBriefInfo(Pageable pageable) {
        List<BookBriefResponse> lists = from(book)
                .join(image)
                .on(image.book.eq(book))
                .join(image.imageStatus, imageStatus)
                .where(imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                .select(Projections.constructor(
                        BookBriefResponse.class,
                        book.id,
                        Projections.constructor(
                                ImageResponse.class,
                                image.path,
                                image.fileName,
                                image.extension),
                        book.name,
                        book.originalCost,
                        book.saleCost
                )).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(book).fetchCount();

        return new PageImpl<>(lists, pageable, total);
    }


    @Override
    public Page<BookBriefResponse> getActiveBookBriefInfo(Pageable pageable) {
        List<BookBriefResponse> lists =
                from(book)
                        .join(book.bookStatus, bookStatus)
                        .join(image)
                        .on(image.book.eq(book))
                        .join(image.imageStatus, imageStatus)
                        .select(Projections.constructor(BookBriefResponse.class,
                                book.id,
                                Projections.constructor(ImageResponse.class,
                                        image.path,
                                        image.fileName,
                                        image.extension),
                                book.name,
                                book.originalCost,
                                book.saleCost))
                        .where(bookStatus.id.in("판매중", "재고없음"))
                        .where(imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = from(book)
                .join(book.bookStatus, bookStatus)
                .where(bookStatus.id.in("판매중", "재고없음"))
                .fetchCount();

        return new PageImpl<>(lists, pageable, total);
    }

    @Override
    public List<BookGetResponseForCoupon> getBookForCoupon() {
        return from(book)
                .select(Projections.constructor(BookGetResponseForCoupon.class, book.id, book.name))
                .where(book.bookStatus.id.in("판매중", "재고없음"))
                .fetch();
    }

    @Override
    public BookResponseForOrder getBookForOrder(Long bookId) {
        return from(book)
                .select(Projections.constructor(BookResponseForOrder.class, book.name, book.saleCost, book.originalCost,
                        book.discountRate, book.isPackaging, book.stock))
                .where(book.id.eq(bookId))
                .fetchOne();
    }

    @Override
    public void updateBookViewCount(Long bookId, Integer count) {
        update(book)
                .where(book.id.eq(bookId))
                .set(book.viewCount, book.viewCount.add(count))
                .execute();
    }
}
