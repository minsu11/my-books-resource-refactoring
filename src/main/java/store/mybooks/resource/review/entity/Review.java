package store.mybooks.resource.review.entity;

import java.time.LocalDate;
import javax.persistence.*;
import store.mybooks.resource.order_detail.entity.OrderDetail;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.review.entity
 * fileName       : Review
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Entity
@Table(name = "review")
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
    
    @Column(name = "review_rate")
    private Integer rate;

    @Column(name = "review_date")
    private LocalDate date;

    @Column(name = "review_title")
    private String title;

    @Column(name = "review_content")
    private String content;


}
