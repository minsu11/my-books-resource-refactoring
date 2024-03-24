package store.mybooks.resource.coupon.utils;

import store.mybooks.resource.coupon.dto.request.CouponCreateRequest;
import store.mybooks.resource.coupon.exception.CouponDateIncorrectException;
import store.mybooks.resource.coupon.exception.CouponInCompatibleType;
import store.mybooks.resource.coupon.exception.OrderMinLessThanDiscountCostException;

/**
 * packageName    : store.mybooks.resource.error
 * fileName       : CouponUtils
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
public class CouponUtils {
    private CouponUtils() {
    }

    /**
     * methodName : validateCouponCreateRequest <br>
     * author : damho-lee <br>
     * description : CouponCreateRequest 검사.<br>
     *
     * @param request CouponCreateRequest
     */
    public static void validateCouponCreateRequest(CouponCreateRequest request) {
        validateCouponDate(request);
        validateCouponType(request);
        validateCouponTarget(request);
        validateCouponOrderMin(request);
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 할인 금액이 null 이 아닌 경우 orderMin > discountCost 인지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @throw OrderMinLessThanDiscountCostException orderMin <= discountCost 인 경우
     */
    private static void validateCouponOrderMin(CouponCreateRequest request) {
        if (request.getDiscountCost() != null && request.getOrderMin() <= request.getDiscountCost()) {
            throw new OrderMinLessThanDiscountCostException(request.getOrderMin(), request.getDiscountCost());
        }
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 시작일과 종료일이 유효한지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @throw CouponDateIncorrectException 시작일이 종료일보다 나중인 경우.
     */
    private static void validateCouponDate(CouponCreateRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new CouponDateIncorrectException(request.getStartDate(), request.getEndDate());
        }
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 타입이 유효한지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @throw CouponInCompatibleType 정률할인, 정액할인쿠폰 둘 다 아닌 경우.
     */
    private static void validateCouponType(CouponCreateRequest request) {
        if (!isPercentageCoupon(request) && !isFlatCoupon(request)) {
            throw new CouponInCompatibleType();
        }
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 적용 대상이 유효한지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @throw CouponInCompatibleType 쿠폰 적용 대상이 전체, 도서, 카테고리 에 만족하지 않는 경우.
     */
    private static void validateCouponTarget(CouponCreateRequest request) {
        if (!isTotalCoupon(request) && !isBookCoupon(request) && !isCategoryCoupon(request)) {
            throw new CouponInCompatibleType();
        }
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 전체 쿠폰인지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @return boolean
     */
    private static boolean isTotalCoupon(CouponCreateRequest request) {
        return request.getBookId() == null && request.getCategoryId() == null;
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 도서 쿠폰인지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @return boolean
     */
    private static boolean isBookCoupon(CouponCreateRequest request) {
        return request.getBookId() != null && request.getCategoryId() == null;
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 카테고리 쿠폰인지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @return boolean
     */
    private static boolean isCategoryCoupon(CouponCreateRequest request) {
        return request.getBookId() == null && request.getCategoryId() != null;
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 정률 할인 쿠폰인지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @return boolean
     */
    private static boolean isPercentageCoupon(CouponCreateRequest request) {
        return request.getDiscountRate() != null && request.getMaxDiscountCost() != null
                && request.getDiscountCost() == null;
    }

    /**
     * methodName : isCategoryFlatDiscountCoupon <br>
     * author : damho-lee <br>
     * description : 정액 할인 쿠폰인지 검사.<br>
     *
     * @param request CouponCreateRequest
     * @return boolean
     */
    private static boolean isFlatCoupon(CouponCreateRequest request) {
        return request.getDiscountRate() == null && request.getMaxDiscountCost() == null
                && request.getDiscountCost() != null;
    }
}
