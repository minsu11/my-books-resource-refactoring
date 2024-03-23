package store.mybooks.resource.orderdetail.enumulation;

import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.order_detail.enumulation<br>
 * fileName       : OrderDetailStatusName<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Getter
public enum OrderDetailStatusName {
    WAIT("주문 대기"),
    PAY_COMPLETE("결제 완료"),
    DELIVERING("배송중"),
    DELIVERY_COMPLETE("배송 완료"),
    PURCHASE_CONFIRMATION("구매 확정");
    private final String value;

    OrderDetailStatusName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
