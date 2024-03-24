package store.mybooks.resource.review.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.image.entity.QImage;
import store.mybooks.resource.image_status.entity.QImageStatus;
import store.mybooks.resource.orderdetail.entity.QOrderDetail;
import store.mybooks.resource.review.dto.response.ReviewDetailGetResponse;
import store.mybooks.resource.review.dto.response.ReviewGetResponse;
import store.mybooks.resource.review.dto.response.ReviewRateResponse;
import store.mybooks.resource.review.entity.QReview;
import store.mybooks.resource.review.entity.Review;
import store.mybooks.resource.user.entity.QUser;

/**
 * packageName    : store.mybooks.resource.review.repository<br>
 * fileName       : ReviewRepositoryImpl<br>
 * author         : masiljangajji<br>
 * date           : 3/17/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/17/24        masiljangajji       최초 생성
 */
public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom {

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    QReview review = QReview.review;

    QImage image = QImage.image;

    QImageStatus imageStatus = QImageStatus.imageStatus;

    QUser user = QUser.user;

    QBook book = QBook.book;

    QOrderDetail orderDetail = QOrderDetail.orderDetail;


    @Override
    public Page<ReviewGetResponse> getReviewByUserId(Long userId, Pageable pageable) {


        List<ReviewGetResponse> lists = from(user)
                .join(review)
                .on(review.user.eq(user))
                .join(orderDetail)
                .on(orderDetail.eq(review.orderDetail))
                .join(book)
                .on(orderDetail.book.eq(book))
                .leftJoin(image)
                .on(image.review.eq(review))
                .where(
                        user.id.eq(userId)
                )
                .select(Projections.constructor(
                        ReviewGetResponse.class,
                        book.id,
                        book.name,
                        review.id,
                        user.name,
                        review.rate,
                        review.date,
                        review.title,
                        review.content,
                        image.path.concat(image.fileName).concat(image.extension)
                )).groupBy(review, image, book, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = from(review)
                .where(review.user.id.eq(userId))
                .fetchCount();

        return new PageImpl<>(lists, pageable, total);
    }

    @Override
    public Page<ReviewDetailGetResponse> getReviewByBookId(Long bookId, Pageable pageable) {


        List<ReviewDetailGetResponse> lists = from(book)
                .join(orderDetail)
                .on(orderDetail.book.eq(book))
                .join(review)
                .on(orderDetail.eq(review.orderDetail))
                .join(user)
                .on(user.eq(review.user))
                .leftJoin(image)
                .on(image.review.eq(review))
                .where(book.id.eq(bookId))
                .select(Projections.constructor(
                        ReviewDetailGetResponse.class,
                        review.id,
                        user.name,
                        review.rate,
                        review.date,
                        review.title,
                        review.content,
                        image.path.concat(image.fileName).concat(image.extension)
                )).groupBy(image, user, review)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = from(review)
                .join(orderDetail)
                .on(review.orderDetail.eq(orderDetail))
                .join(book)
                .on(orderDetail.book.eq(book))
                .where(book.id.eq(bookId))
                .fetchCount();

        return new PageImpl<>(lists, pageable, total);

    }

    @Override
    public Optional<ReviewGetResponse> getReview(Long reviewId) {


        ReviewGetResponse response = from(review)
                .leftJoin(image)
                .on(image.review.eq(review))
                .join(orderDetail)
                .on(orderDetail.eq(review.orderDetail))
                .join(book)
                .on(orderDetail.book.eq(book))
                .join(user)
                .on(user.eq(review.user))
                .select(Projections.constructor(
                        ReviewGetResponse.class,
                        book.id,
                        book.name,
                        review.id,
                        user.name,
                        review.rate,
                        review.date,
                        review.title,
                        review.content,
                        image.path.concat(image.fileName).concat(image.extension)
                ))
                .groupBy(review,user,image,book)
                .where(review.id.eq(reviewId))
                .fetchOne();


        return Optional.of(response);
    }

    @Override
    public ReviewRateResponse getReviewRate(Long bookId) {


        ReviewRateResponse response = from(review)
                .join(review.orderDetail, orderDetail)
                .join(orderDetail.book, book)
                .where(book.id.eq(bookId))
                .select(Projections.constructor(
                        ReviewRateResponse.class,
                        review.count(),
                        review.rate.avg().coalesce(0.0) // 평균 평점
                ))
                .groupBy(book)
                .fetchOne();

        if (Objects.isNull(response)) {
            return new ReviewRateResponse(0L, 0.0);
        }

        return response;
    }
}
