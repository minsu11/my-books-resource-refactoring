package store.mybooks.resource.user_coupon.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book.entity.QBook;
import store.mybooks.resource.category.entity.QCategory;
import store.mybooks.resource.coupon.entity.QCoupon;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForQuerydsl;
import store.mybooks.resource.user_coupon.entity.QUserCoupon;
import store.mybooks.resource.user_coupon.entity.UserCoupon;

/**
 * packageName    : store.mybooks.resource.user_coupon.repository
 * fileName       : UserCouponRepositoryImpl
 * author         : damho-lee
 * date           : 3/5/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/5/24          damho-lee          최초 생성
 */
public class UserCouponRepositoryImpl extends QuerydslRepositorySupport implements UserCouponRepositoryCustom {
    public UserCouponRepositoryImpl() {
        super(UserCoupon.class);
    }


    @Override
    public Page<UserCouponGetResponseForQuerydsl> getUserCoupons(Long userId, Pageable pageable) {
        QUserCoupon userCoupon = QUserCoupon.userCoupon;
        QCoupon coupon = QCoupon.coupon;
        QBook book = QBook.book;
        QCategory category = QCategory.category;

        List<UserCouponGetResponseForQuerydsl> userCouponGetResponseList =
                from(userCoupon)
                        .leftJoin(coupon)
                        .on(userCoupon.coupon.id.eq(coupon.id))
                        .leftJoin(book)
                        .on(coupon.book.id.eq(book.id))
                        .leftJoin(category)
                        .on(coupon.category.id.eq(category.id))
                        .where(userCoupon.user.id.eq(userId))
                        .select(Projections.constructor(UserCouponGetResponseForQuerydsl.class,
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

        long total = from(userCoupon)
                .where(userCoupon.user.id.eq(userId))
                .fetchCount();

        return new PageImpl<>(userCouponGetResponseList, pageable, total);
    }
}
