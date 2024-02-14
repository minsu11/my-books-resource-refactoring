package store.mybooks.resource.coupon.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import store.mybooks.resource.category.entity.Category;

/**
 * packageName    : store.mybooks.resource.domain.entity
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
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
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

    @Column(name = "coupon_created_at")
    private LocalDate createdAt;

}
