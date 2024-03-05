package store.mybooks.resource.coupon.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.category.repository.CategoryRepository;
import store.mybooks.resource.coupon.dto.request.CouponCreateRequest;
import store.mybooks.resource.coupon.dto.response.CouponGetResponse;
import store.mybooks.resource.coupon.dto.response.CouponGetResponseForQuerydsl;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.coupon.exception.CouponCannotDeleteException;
import store.mybooks.resource.coupon.exception.CouponNotExistsException;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.coupon.utils.CouponUtils;
import store.mybooks.resource.user_coupon.repository.UserCouponRepository;

/**
 * packageName    : store.mybooks.resource.coupon.entity.service
 * fileName       : CouponService
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {
    private final CouponRepository couponRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional(readOnly = true)
    public Page<CouponGetResponse> getCoupons(Pageable pageable) {
        Page<CouponGetResponseForQuerydsl> couponPage = couponRepository.getCoupons(pageable);
        List<CouponGetResponse> couponGetResponseList = new ArrayList<>();

        for (CouponGetResponseForQuerydsl response : couponPage.getContent()) {
            couponGetResponseList.add(makeCouponGetResponse(response));
        }

        return new PageImpl<>(couponGetResponseList, couponPage.getPageable(), couponPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<CouponGetResponse> getCoupons(Pageable pageable) {
        Page<CouponGetResponseForQuerydsl> couponPage = couponRepository.getCoupons(pageable);
        List<CouponGetResponse> couponGetResponseList = new ArrayList<>();

        for (CouponGetResponseForQuerydsl response : couponPage.getContent()) {
            couponGetResponseList.add(makeCouponGetResponse(response));
        }

        return new PageImpl<>(couponGetResponseList, couponPage.getPageable(), couponPage.getTotalElements());
    }

    /**
     * methodName : createCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 생성.<br>
     *
     * @param request CouponCreateRequest
     */
    public void createCoupon(CouponCreateRequest request) {
        CouponUtils.validateCouponCreateRequest(request);

        Book book = request.getBookId() == null ? null : findBook(request.getBookId());
        Category category = request.getCategoryId() == null ? null : findCategory(request.getCategoryId());

        Coupon coupon = new Coupon(
                request.getName(),
                book,
                category,
                request.getOrderMin(),
                request.getDiscountCost(),
                request.getMaxDiscountCost(),
                request.getDiscountRate(),
                request.getStartDate(),
                request.getEndDate(),
                request.getDiscountRate() != null,
                request.getBookId() == null && request.getCategoryId() == null
        );

        couponRepository.save(coupon);
    }

    private CouponGetResponse makeCouponGetResponse(CouponGetResponseForQuerydsl response) {
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

        return new CouponGetResponse(
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

    /**
     * methodName : deleteCoupon <br>
     * author : damho-lee <br>
     * description : 쿠폰 삭제. 쿠폰을 받은 회원이 있다면 지울 수 없다.<br>
     *
     * @param id Long
     */
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new CouponNotExistsException(id);
        }

        if (userCouponRepository.countByCoupon_Id(id) > 0) {
            throw new CouponCannotDeleteException(id);
        }

        couponRepository.deleteById(id);
    }

    private Book findBook(long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));
    }

    private Category findCategory(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotExistsException(categoryId));
    }
}
