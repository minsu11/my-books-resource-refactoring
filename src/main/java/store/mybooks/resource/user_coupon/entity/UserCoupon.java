package store.mybooks.resource.user_coupon.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user_coupon.entity
 * fileName       : UserCoupon
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "user_coupon")
@Getter
@NoArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "user_coupon_created_date")
    private LocalDate createdDate;

    @Column(name = "user_coupon_date")
    private LocalDate date;

    @Column(name = "is_used")
    private Boolean isUsed;

    /**
     * UserCoupon 생성자.
     *
     * @param user   회원
     * @param coupon 쿠폰
     */
    public UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        this.createdDate = LocalDate.now();
        this.date = null;
        this.isUsed = false;
    }

    public void use() {
        this.date = LocalDate.now();
        this.isUsed = true;
    }

    public void giveBack() {
        this.date = null;
        this.isUsed = false;
    }
}
