package store.mybooks.resource.coupon.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.category.entity.QCategory;
import store.mybooks.resource.coupon.dto.response.CouponGetResponseForQuerydsl;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.coupon.entity.QCoupon;

/**
 * packageName    : store.mybooks.resource.coupon.repository
 * fileName       : CouponRepositoryImpl
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
public class CouponRepositoryImpl extends QuerydslRepositorySupport implements CouponRepositoryCustom {
    public CouponRepositoryImpl() {
        super(Coupon.class);
    }

    @Override
    public Page<CouponGetResponseForQuerydsl> getCoupons(Pageable pageable) {
        QCoupon coupon = QCoupon.coupon;
        QBook book = QBook.book;
        QCategory category = QCategory.category;

        List<CouponGetResponseForQuerydsl> responseList = from(coupon)
                .leftJoin(book)
                .on(coupon.book.id.eq(book.id))
                .leftJoin(category)
                .on(coupon.category.id.eq(category.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(coupon.endDate.desc())
                .orderBy(coupon.createdDate.desc())
                .select(Projections.constructor(CouponGetResponseForQuerydsl.class,
                        coupon.id,
                        coupon.name,
                        coupon.orderMin,
                        coupon.discountCost,
                        coupon.maxDiscountCost,
                        coupon.discountRate,
                        coupon.isRate,
                        coupon.startDate,
                        coupon.endDate,
                        book.name,
                        category.name))
                .fetch();

        long total = from(coupon).fetchCount();

        return new PageImpl<>(responseList, pageable, total);
    }
}
