package store.mybooks.resource.user_coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForMyPage;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForMyPageQuerydsl;
import store.mybooks.resource.user_coupon.repository.UserCouponRepository;

/**
 * packageName    : store.mybooks.resource.user_coupon.service
 * fileName       : UserCouponServiceTest
 * author         : damho-lee
 * date           : 3/7/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/24          damho-lee          최초 생성
 */
@ExtendWith(MockitoExtension.class)
class UserCouponServiceTest {
    @Mock
    UserCouponRepository userCouponRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    UserCouponService userCouponService;

    @Test
    @DisplayName("회원 쿠폰 조회 테스트")
    void givenUserIdAndPageable_whenGetUserCoupons_thenReturnPageOfUserCouponGetResponseForMyPage() {
        List<UserCouponGetResponseForMyPageQuerydsl> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 2);
        UserCouponGetResponseForMyPageQuerydsl firstUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        1L,
                        "firstUserCoupon",
                        10000,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );
        UserCouponGetResponseForMyPageQuerydsl secondUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        2L,
                        "secondUserCoupon",
                        10000,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );
        UserCouponGetResponseForMyPageQuerydsl thirdUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        3L,
                        "thirdUserCoupon",
                        10000,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );
        UserCouponGetResponseForMyPageQuerydsl fourthUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        4L,
                        "fourthUserCoupon",
                        10000,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        null
                );
        list.add(firstUserCoupon);
        list.add(secondUserCoupon);
        list.add(thirdUserCoupon);
        list.add(fourthUserCoupon);
        int offset = (int) pageable.getOffset();
        int size = pageable.getPageSize();
        Page<UserCouponGetResponseForMyPageQuerydsl> userCouponGetResponsesPage =
                new PageImpl<>(list.subList(offset, offset + size), pageable, list.size());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userCouponRepository.getUserCoupons(anyLong(), any())).thenReturn(userCouponGetResponsesPage);

        Page<UserCouponGetResponseForMyPage> actualPage = userCouponService.getUserCoupons(1L, pageable);
        List<UserCouponGetResponseForMyPage> actualList = actualPage.getContent();

        assertThat(actualPage).isNotNull();
        assertThat(actualList).isNotNull().hasSize(2);
        assertThat(actualList.get(0).getUserCouponId()).isNotNull().isEqualTo(thirdUserCoupon.getId());
        assertThat(actualList.get(1).getUserCouponId()).isNotNull().isEqualTo(fourthUserCoupon.getId());
        assertThat(actualPage.getTotalElements()).isEqualTo(userCouponGetResponsesPage.getTotalElements());
    }
}