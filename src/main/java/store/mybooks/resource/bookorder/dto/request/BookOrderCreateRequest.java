package store.mybooks.resource.bookorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * packageName    : store.mybooks.resource.book_order.dto.request
 * fileName       : BookOrderRequest
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Getter
@AllArgsConstructor
public class BookOrderCreateRequest {
    @NonNull
    private Long userId;
    private Long userCouponId;
    private Long wrapId;
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
