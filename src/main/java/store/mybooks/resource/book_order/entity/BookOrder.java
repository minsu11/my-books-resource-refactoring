package store.mybooks.resource.book_order.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_order")
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_out_date")
    private LocalDate outDate;

    @CreatedDate
    @Column(name = "order_date")
    private LocalDate date;

    @Column(name = "order_invoice_number")
    private String invoiceNumber;

    @Column(name = "order_receiver_name")
    private String receiverName;

    @Column(name = "order_receiver_address")
    private String receiverAddress;

    @Column(name = "order_receiver_phonenumber")
    private String receiverPhoneNumber;

    @Column(name = "order_receiver_message")
    private String receiverMessage;

    @Column(name = "order_total_cost")
    private Integer totalCost;

    @Column(name = "order_point_cost")
    private Integer pointCost;

    @Column(name = "order_coupon_cost")
    private Integer couponCost;

    @Column(name = "is_coupon_used")
    private Boolean isCouponUsed;

    @Column(name = "order_number")
    private String number;

    @Column(name = "order_find_password")
    private String findPassword;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "orders_status_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrdersStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_rule_id")
    private DeliveryRule deliveryRule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wrap_id")
    private Wrap wrap;

    public BookOrder(Long id, String invoiceNumber, String receiverName, String receiverAddress,
                     String receiverPhoneNumber, String receiverMessage, Integer totalCost, Integer pointCost, Integer couponCost, Boolean isCouponUsed,
                     String number, String findPassword, User user, OrdersStatus orderStatus, DeliveryRule deliveryRule, UserCoupon userCoupon, Wrap wrap) {
        this.id = id;
        this.outDate = null;
        this.invoiceNumber = invoiceNumber;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.receiverMessage = receiverMessage;
        this.totalCost = totalCost;
        this.pointCost = pointCost;
        this.couponCost = couponCost;
        this.isCouponUsed = isCouponUsed;
        this.number = number;
        this.findPassword = findPassword;
        this.user = user;
        this.orderStatus = orderStatus;
        this.deliveryRule = deliveryRule;
        this.userCoupon = userCoupon;
        this.wrap = wrap;
    }

    public void modifyBookOrderAdmin(OrdersStatus ordersStatus) {
        this.orderStatus = ordersStatus;
        this.outDate = LocalDate.now();
    }
}