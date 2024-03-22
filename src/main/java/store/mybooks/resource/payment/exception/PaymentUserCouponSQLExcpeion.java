package store.mybooks.resource.payment.exception;

/**
 * packageName    : store.mybooks.resource.payment.exception<br>
 * fileName       : PaymentUserCouponSQLExcpeion<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
public class PaymentUserCouponSQLExcpeion extends RuntimeException {
    public PaymentUserCouponSQLExcpeion() {
        super("sql 업데이트 중 유저 쿠폰에서 에러가 남");
    }
}
