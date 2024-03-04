package store.mybooks.resource.user_coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user_coupon.service.UserCouponService;

/**
 * packageName    : store.mybooks.resource.user_coupon.controller
 * fileName       : UserCouponRestController
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-coupon")
public class UserCouponRestController {
    private final UserCouponService userCouponService;
}
