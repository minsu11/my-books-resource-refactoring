package store.mybooks.resource.returndetail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import store.mybooks.resource.book_return.entity.BookReturn;
import store.mybooks.resource.orderdetail.entity.OrderDetail;

/**
 * packageName    : store.mybooks.resource.return_detail.entity
 * fileName       : ReturnDetail
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
@Table(name = "return_detail")
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_detail_id")
    private Long id;

    @Column(name = "return_detail_amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "book_return_id")
    private BookReturn bookReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;


}
