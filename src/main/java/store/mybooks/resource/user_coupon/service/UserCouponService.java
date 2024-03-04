package store.mybooks.resource.user_coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.coupon.exception.CouponNotExistsException;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_coupon.dto.request.UserCouponCreateRequest;
import store.mybooks.resource.user_coupon.entity.UserCoupon;
import store.mybooks.resource.user_coupon.exception.UserCouponNotExistsException;
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
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    /**
     * methodName : createUserCoupon <br>
     * author : damho-lee <br>
     * description : 회원 쿠폰 생성.<br>
     *
     * @param request UserCouponCreateRequest
     */
    public void createUserCoupon(UserCouponCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotExistException(request.getUserId()));
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new CouponNotExistsException(request.getCouponId()));

        UserCoupon userCoupon = new UserCoupon(user, coupon);
        userCouponRepository.save(userCoupon);
    }


    /**
     * methodName : useUserCoupon <br>
     * author : damho-lee <br>
     * description : 회원 쿠폰 사용.<br>
     *
     * @param id Long
     */
    public void useUserCoupon(Long id) {
        UserCoupon userCoupon =
                userCouponRepository.findById(id).orElseThrow(() -> new UserCouponNotExistsException(id));
        userCoupon.use();
    }

    /**
     * methodName : giveBackUserCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 돌려주는 메서드.<br>
     *
     * @param id Long
     */
    public void giveBackUserCoupon(Long id) {
        UserCoupon userCoupon =
                userCouponRepository.findById(id).orElseThrow(() -> new UserCouponNotExistsException(id));
        userCoupon.giveBack();
    }
}
