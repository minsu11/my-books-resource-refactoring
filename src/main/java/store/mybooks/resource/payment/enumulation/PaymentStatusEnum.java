package store.mybooks.resource.payment.enumulation;

import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.payment.enumulation<br>
 * fileName       : PaymentStatusEnum<br>
 * author         : minsu11<br>
 * date           : 3/25/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/25/24        minsu11       최초 생성<br>
 */
@Getter
public enum PaymentStatusEnum {
    DONE("결제 완료", "DONE"),
    CANCELED("결제 취소", "CANCELED");
    private final String korPaymentStatus;
    private final String engPaymentStatus;

    PaymentStatusEnum(String korPaymentStatus, String engPaymentStatus) {
        this.korPaymentStatus = korPaymentStatus;
        this.engPaymentStatus = engPaymentStatus;
    }
}
