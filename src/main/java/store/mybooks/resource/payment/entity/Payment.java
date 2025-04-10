package store.mybooks.resource.payment.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.bookorder.entity.BookOrder;

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
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_created_at")
    private LocalDateTime createdAt;

    @Column(name = "payment_order_number")
    private String orderNumber;

    @Column(name = "payment_status")
    private String status;

    @Column(name = "payment_buyer")
    private String buyer;

    @Column(name = "payment_cost")
    private Integer cost;

    @Column(name = "payment_type")
    private String type;

    @OneToOne
    @JoinColumn(name = "order_id")
    private BookOrder bookOrder;

    public void update(String status) {
        this.status = status;
    }
}
