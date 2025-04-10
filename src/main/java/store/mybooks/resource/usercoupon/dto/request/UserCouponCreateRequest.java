package store.mybooks.resource.usercoupon.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user_coupon.dto.request
 * fileName       : UserCouponCreateRequest
 * author         : damho-lee
 * date           : 3/4/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/4/24          damho-lee          최초 생성
 */
@Getter
@NoArgsConstructor
public class UserCouponCreateRequest {
    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Long couponId;
}
