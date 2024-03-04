package store.mybooks.resource.user_coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.user_coupon.repository.UserCouponRepository;

/**
 * packageName    : store.mybooks.resource.user_coupon.service
 * fileName       : UserCouponService
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
}
