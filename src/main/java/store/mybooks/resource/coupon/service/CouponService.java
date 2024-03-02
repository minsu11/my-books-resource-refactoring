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
import store.mybooks.resource.coupon.dto.request.BookFlatDiscountCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.BookPercentageCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.CategoryFlatDiscountCouponRequest;
import store.mybooks.resource.coupon.dto.request.CategoryPercentageCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.FlatDiscountCouponCreateRequest;
import store.mybooks.resource.coupon.dto.request.TotalPercentageCouponCreateRequest;
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

    public void createTotalPercentageCoupon(TotalPercentageCouponCreateRequest request) {
        CouponUtils.validateCouponDate(request.getStartDate(), request.getEndDate());
        Coupon coupon = Coupon.makeTotalPercentageCoupon(
                request.getName(),
                request.getOrderMin(),
                request.getMaxDiscountCost(),
                request.getDiscountRate(),
                request.getStartDate(),
                request.getEndDate());

        couponRepository.save(coupon);
    }

    public void createFlatDiscountCoupon(FlatDiscountCouponCreateRequest request) {
        CouponUtils.validateCouponDate(request.getStartDate(), request.getEndDate());
        CouponUtils.validateCouponOrderMin(request.getOrderMin(), request.getDiscountCost());
        Coupon coupon = Coupon.makeFlatDiscountCoupon(
                request.getName(),
                request.getOrderMin(),
                request.getDiscountCost(),
                request.getStartDate(),
                request.getEndDate()
        );

        couponRepository.save(coupon);
    }

    public void createBookPercentageCoupon(BookPercentageCouponCreateRequest request) {
        CouponUtils.validateCouponDate(request.getStartDate(), request.getEndDate());
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(
                () -> new BookNotExistException(request.getBookId()));
        Coupon coupon = Coupon.makeBookPercentageCoupon(
                request.getName(),
                book,
                request.getOrderMin(),
                request.getMaxDiscountCost(),
                request.getDiscountRate(),
                request.getStartDate(),
                request.getEndDate()
        );

        couponRepository.save(coupon);
    }

    public void createBookFlatDiscountCoupon(BookFlatDiscountCouponCreateRequest request) {
        CouponUtils.validateCouponDate(request.getStartDate(), request.getEndDate());
        CouponUtils.validateCouponOrderMin(request.getOrderMin(), request.getDiscountCost());
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(
                () -> new BookNotExistException(request.getBookId()));
        Coupon coupon = Coupon.makeBookFlatDiscountCoupon(
                request.getName(),
                book,
                request.getOrderMin(),
                request.getDiscountCost(),
                request.getStartDate(),
                request.getEndDate()
        );

        couponRepository.save(coupon);
    }

    public void createCategoryPercentageCoupon(CategoryPercentageCouponCreateRequest request) {
        CouponUtils.validateCouponDate(request.getStartDate(), request.getEndDate());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new CategoryNotExistsException(request.getCategoryId()));
        Coupon coupon = Coupon.makeCategoryPercentageCoupon(
                request.getName(),
                category,
                request.getOrderMin(),
                request.getMaxDiscountCost(),
                request.getDiscountRate(),
                request.getStartDate(),
                request.getEndDate()
        );

        couponRepository.save(coupon);
    }

    public void createCategoryFlatDiscountCoupon(CategoryFlatDiscountCouponRequest request) {
        CouponUtils.validateCouponDate(request.getStartDate(), request.getEndDate());
        CouponUtils.validateCouponOrderMin(request.getOrderMin(), request.getDiscountCost());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new CategoryNotExistsException(request.getCategoryId()));
        Coupon coupon = Coupon.makeCategoryFlatDiscountCoupon(
                request.getName(),
                category,
                request.getOrderMin(),
                request.getDiscountCost(),
                request.getStartDate(),
                request.getEndDate()
        );

        couponRepository.save(coupon);
    }
}
