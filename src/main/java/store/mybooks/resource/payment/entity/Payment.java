package store.mybooks.resource.payment.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import store.mybooks.resource.book_order.entity.BookOrder;

/**
 * packageName    : store.mybooks.resource.payment.entity
 * fileName       : Payment
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
@Table(name = "payment")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_datetime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "payment_order_number", nullable = false)
    private String orderNumber;

    @Column(name = "payment_status", nullable = false)
    private String status;

    @Column(name = "payment_buyer", nullable = false)
    private String buyer;

    @Column(name = "payment_cost", nullable = false)
    private Integer cost;

    @Column(name = "payment_type", nullable = false)
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private BookOrder bookOrder;

}
