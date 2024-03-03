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

    /**
     * Coupon 생성자.
     *
     * @param name            이름
     * @param book            도서
     * @param category        카테고리
     * @param orderMin        최소주문금액
     * @param discountCost    할인금액
     * @param maxDiscountCost 최대할인금액
     * @param discountRate    할인율
     * @param startDate       시작일
     * @param endDate         종료일
     * @param isRate          정률할인쿠폰인지
     * @param isTargetOrder   전체적용쿠폰인지
     */
    public Coupon(String name, Book book, Category category, Integer orderMin, Integer discountCost,
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
}
