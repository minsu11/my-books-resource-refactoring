package store.mybooks.resource.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.category.repository.CategoryRepository;
import store.mybooks.resource.coupon.dto.request.CouponCreateRequest;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.coupon.utils.CouponUtils;

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

    private Book findBook(long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotExistException(bookId));
    }

    private Category findCategory(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotExistsException(categoryId));
    }
}
