package store.mybooks.resource.book_order.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.orders_status.entity.OrdersStatus;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_coupon.entity.UserCoupon;
import store.mybooks.resource.wrap.entity.Wrap;


/**
 * packageName    : store.mybooks.resource.bookorder.entity
 * fileName       : BookOrder
 * author         : minsu11
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        minsu11       최초 생성
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_order")
public class BookOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_outdate", nullable = false)
    private LocalDate outDate;

    @Column(name = "order_date", nullable = false)
    private LocalDate date;

    @Column(name = "order_invoice_number")
    private String invoiceNumber;

    @Column(name = "order_receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "order_receiver_address", nullable = false)
    private String receiverAddress;

    @Column(name = "order_receiver_phonenumber", nullable = false)
    private String receiverPhoneNumber;

    @Column(name = "order_receiver_message")
    private String receiverMessage;

    @Column(name = "order_total_cost", nullable = false)
    private Integer totalCost;

    @Column(name = "order_point_cost")
    private Integer pointCost;

    @Column(name = "order_coupon_cost")
    private Integer couponCost;

    @Column(name = "is_coupon_used")
    private Boolean isCouponUsed;

    @Column(name = "order_number", nullable = false)
    private String number;

    @Column(name = "order_find_password")
    private String findPassword;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "orders_status_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrdersStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "delivery_rule_id", nullable = false)
    private DeliveryRule deliveryRule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    @ManyToOne
    @JoinColumn(name = "wrap_id")
    private Wrap wrap;

}