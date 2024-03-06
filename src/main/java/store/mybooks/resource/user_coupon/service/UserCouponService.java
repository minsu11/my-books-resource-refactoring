package store.mybooks.resource.user_coupon.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.coupon.exception.CouponNotExistsException;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_coupon.dto.request.UserCouponCreateRequest;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponse;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForQuerydsl;
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
     * methodName : getUserCoupons <br>
     * author : damho-lee <br>
     * description : 회원 쿠폰 페이지 요청. <br>
     *
     * @param userId   Long
     * @param pageable Pageable
     * @return page
     */
    public Page<UserCouponGetResponse> getUserCoupons(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }

        Page<UserCouponGetResponseForQuerydsl> userCouponGetResponsesPage =
                userCouponRepository.getUserCoupons(userId, pageable);
        List<UserCouponGetResponse> userCouponGetResponseList = new ArrayList<>();

        for (UserCouponGetResponseForQuerydsl userCouponGetResponseForQuerydsl : userCouponGetResponsesPage.getContent()) {
            userCouponGetResponseList.add(makeCouponGetResponse(userCouponGetResponseForQuerydsl));
        }

        return new PageImpl<>(
                userCouponGetResponseList,
                userCouponGetResponsesPage.getPageable(),
                userCouponGetResponsesPage.getTotalElements());
    }

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

    private UserCouponGetResponse makeCouponGetResponse(UserCouponGetResponseForQuerydsl response) {
        Integer discountRateOrCost =
                response.getIsRate() ? response.getDiscountRate() : response.getDiscountCost();
        Integer maxDiscountCost =
                response.getIsRate() ?  response.getMaxDiscountCost() : response.getDiscountCost();

        String range;
        String target;
        if (response.getCategoryName() == null && response.getBookName() == null) {
            range = "전체";
            target = "전체 도서";
        } else if (response.getBookName() != null) {
            range = "도서";
            target = response.getBookName();
        } else {
            range = "카테고리";
            target = response.getCategoryName();
        }

        return new UserCouponGetResponse(
                response.getId(),
                response.getName(),
                range,
                target,
                response.getOrderMin(),
                discountRateOrCost,
                maxDiscountCost,
                response.getIsRate(),
                response.getStartDate(),
                response.getEndDate()
        );
    }
}
