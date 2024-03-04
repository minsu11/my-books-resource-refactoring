package store.mybooks.resource.coupon.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.coupon.dto.request.CouponCreateRequest;
import store.mybooks.resource.coupon.service.CouponService;
import store.mybooks.resource.error.Utils;

/**
 * packageName    : store.mybooks.resource.coupon.entity.controller
 * fileName       : CouponRestController
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponRestController {
    private final CouponService couponService;

    /**
     * methodName : createTotalPercentageCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 생성.<br>
     *
     * @param request CouponCreateRequest
     * @param bindingResult BindingResult
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Void> createTotalPercentageCoupon(
            @Valid @RequestBody CouponCreateRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
