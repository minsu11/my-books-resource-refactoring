package store.mybooks.resource.coupon.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.NoArgsConstructor;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.category.entity.Category;

/**
 * packageName    : store.mybooks.resource.coupon.entity
 * fileName       : Coupon
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "coupon")
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "coupon_order_min")
    private Integer orderMin;

    @Column(name = "coupon_discount_cost")
    private Integer discountCost;

    @Column(name = "coupon_max_discount_cost")
    private Integer maxDiscountCost;

    @Column(name = "coupon_discount_rate")
    private Integer discountRate;

    @Column(name = "coupon_start_date")
    private LocalDate startDate;

    @Column(name = "coupon_end_date")
    private LocalDate endDate;

    @Column(name = "is_rate")
    private Boolean isRate;

    @Column(name = "is_target_order")
    private Boolean isTargetOrder;

    @Column(name = "coupon_created_date")
    private LocalDate createdDate;

    private Coupon(String name, Book book, Category category, Integer orderMin, Integer discountCost,
                  Integer maxDiscountCost, Integer discountRate, LocalDate startDate, LocalDate endDate, Boolean isRate,
                  Boolean isTargetOrder) {
        this.name = name;
        this.book = book;
        this.category = category;
        this.orderMin = orderMin;
        this.discountCost = discountCost;
        this.maxDiscountCost = maxDiscountCost;
        this.discountRate = discountRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRate = isRate;
        this.isTargetOrder = isTargetOrder;
        this.createdDate = LocalDate.now();
    }

    public static Coupon makeTotalPercentageCoupon(String name, Integer orderMin, Integer maxDiscountCost,
                                                   Integer discountRate, LocalDate startDate, LocalDate endDate) {
        return new Coupon(name, null, null, orderMin, null, maxDiscountCost, discountRate, startDate, endDate, true,
                true);
    }

    public static Coupon makeFlatDiscountCoupon(String name, Integer orderMin, Integer discountCost,
                                                LocalDate startDate, LocalDate endDate) {
        return new Coupon(name, null, null, orderMin, discountCost, null, null, startDate, endDate, false, true);
    }

    public static Coupon makeBookPercentageCoupon(String name, Book book, Integer orderMin,
                                                  Integer maxDiscountCost, Integer discountRate,
                                                  LocalDate startDate, LocalDate endDate) {
        return new Coupon(name, book, null, orderMin, null, maxDiscountCost, discountRate, startDate, endDate, true,
                false);
    }

    public static Coupon makeBookFlatDiscountCoupon(String name, Book book, Integer orderMin, Integer discountCost,
                                                    LocalDate startDate, LocalDate endDate) {
        return new Coupon(name, book, null, orderMin, discountCost, null, null, startDate, endDate, false,
                false);
    }

    public static Coupon makeCategoryPercentageCoupon(String name, Category category, Integer orderMin,
                                                      Integer maxDiscountCost, Integer discountRate,
                                                      LocalDate startDate, LocalDate endDate) {
        return new Coupon(name, null, category, orderMin, null, maxDiscountCost, discountRate, startDate, endDate, true,
                false);
    }

    public static Coupon makeCategoryFlatDiscountCoupon(String name, Category category, Integer orderMin,
                                                        Integer discountCost, LocalDate startDate, LocalDate endDate) {
        return new Coupon(name, null, category, orderMin, discountCost, null, null, startDate, endDate, false,
                false);
    }
}
