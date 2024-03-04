package store.mybooks.resource.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.coupon.dto.response.CouponGetResponseForQuerydsl;

/**
 * packageName    : store.mybooks.resource.coupon.repository
 * fileName       : CouponRepositoryCustom
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
@NoRepositoryBean
public interface CouponRepositoryCustom {
    Page<CouponGetResponseForQuerydsl> getCoupons(Pageable pageable);
}
