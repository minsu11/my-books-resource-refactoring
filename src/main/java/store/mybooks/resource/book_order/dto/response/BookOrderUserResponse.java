package store.mybooks.resource.book_order.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book_order.dto.response
 * fileName       : BookOrderResponse
 * author         : minsu11
 * date           : 2/15/24
 * description    : 회원아이디, 주문 상태 명, 배송 규정 아이디,
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Getter
@AllArgsConstructor
public class BookOrderUserResponse {
    // 주문 내역 조회할 때
    private Long userId;
    private String statusId;
    private Integer deliveryRuleId;
    private Integer wrapId;
    private LocalDate date;
    private String invoiceNumber;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhoneNumber;
    private String receiverMessage;
    private Integer totalCost;
    private Integer pointCost;
    private Integer couponCost;
    private String number;
    private String findPassword;

}
