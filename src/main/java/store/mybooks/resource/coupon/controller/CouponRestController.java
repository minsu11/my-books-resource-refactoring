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
import store.mybooks.resource.coupon.dto.request.BookFlatDiscountCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.BookPercentageCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.CategoryFlatDiscountCouponRequest;
import store.mybooks.resource.coupon.dto.request.CategoryPercentageCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.FlatDiscountCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.TotalPercentageCouponCreateRequest;
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

    @PostMapping("/total-percentage-coupon/register")
    public ResponseEntity<Void> createTotalPercentageCoupon(
            @Valid @RequestBody TotalPercentageCouponCreateRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createTotalPercentageCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/flat-discount-coupon/register")
    public ResponseEntity<Void> createFlatDiscountCoupon(@Valid @RequestBody FlatDiscountCouponCreateRequest request,
                                                         BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createFlatDiscountCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/book-percentage-coupon/register")
    public ResponseEntity<Void> createBookPercentageCoupon(
            @Valid @RequestBody BookPercentageCouponCreateRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createBookPercentageCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/book-flat-discount-coupon/register")
    public ResponseEntity<Void> createBookFlatDiscountCoupon(
            @Valid @RequestBody BookFlatDiscountCouponCreateRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createBookFlatDiscountCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/category-percentage-coupon/register")
    public ResponseEntity<Void> createCategoryPercentageCoupon(
            @Valid @RequestBody CategoryPercentageCouponCreateRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createCategoryPercentageCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/category-flat-discount-coupon/register")
    public ResponseEntity<Void> createCategoryFlatDiscountCoupon(@Valid @RequestBody
                                                                 CategoryFlatDiscountCouponRequest request,
                                                                 BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        couponService.createCategoryFlatDiscountCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
