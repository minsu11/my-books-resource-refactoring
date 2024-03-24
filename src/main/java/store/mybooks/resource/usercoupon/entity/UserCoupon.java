package store.mybooks.resource.usercoupon.entity;

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
import store.mybooks.resource.usercoupon.exception.UserCouponAlreadyUsedException;
import store.mybooks.resource.usercoupon.exception.UserCouponNotUsedException;
import store.mybooks.resource.utils.TimeUtils;

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
        this.createdDate = TimeUtils.nowDate();
        this.date = null;
        this.isUsed = false;
    }

    /**
     * methodName : use <br>
     * author : damho-lee <br>
     * description : 쿠폰 사용 메서드.<br>
     */
    public void use() {
        if (this.date != null && this.isUsed) {
            throw new UserCouponAlreadyUsedException(this.id);
        }

        this.date = TimeUtils.nowDate();
        this.isUsed = true;
    }

    /**
     * methodName : giveBack <br>
     * author : damho-lee <br>
     * description : 쿠폰 되돌려주는 메서드.<br>
     */
    public void giveBack() {
        if (this.date == null && !this.isUsed) {
            throw new UserCouponNotUsedException(this.id);
        }

        this.date = null;
        this.isUsed = false;
    }
}
