package store.mybooks.resource.usercoupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.util.ReflectionTestUtils;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.coupon.exception.CouponNotExistsException;
import store.mybooks.resource.coupon.repository.CouponRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.usercoupon.dto.request.UserCouponCreateRequest;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForMyPage;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForMyPageQuerydsl;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForOrder;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForOrderQuerydsl;
import store.mybooks.resource.usercoupon.entity.UserCoupon;
import store.mybooks.resource.usercoupon.exception.UserCouponNotExistsException;
import store.mybooks.resource.usercoupon.repository.UserCouponRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_status.entity.UserStatus;

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
    @DisplayName("회원 쿠폰 조회")
    void givenUserIdAndPageable_whenGetUserCoupons_thenReturnPageOfUserCouponGetResponseForMyPage() {
        List<UserCouponGetResponseForMyPageQuerydsl> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 3);
        UserCouponGetResponseForMyPageQuerydsl firstUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        1L,
                        "firstUserCoupon",
                        0,
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
                        0,
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
                        0,
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
                        0,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        "이순신",
                        null
                );
        UserCouponGetResponseForMyPageQuerydsl fifthUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        4L,
                        "fifthUserCoupon",
                        10000,
                        null,
                        5000,
                        50,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2),
                        null,
                        "위인전"
                );
        UserCouponGetResponseForMyPageQuerydsl sixthUserCoupon =
                new UserCouponGetResponseForMyPageQuerydsl(
                        4L,
                        "sixthUserCoupon",
                        0,
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
        list.add(fifthUserCoupon);
        list.add(sixthUserCoupon);
        int offset = (int) pageable.getOffset();
        int size = pageable.getPageSize();
        Page<UserCouponGetResponseForMyPageQuerydsl> userCouponGetResponsesPage =
                new PageImpl<>(list.subList(offset, offset + size), pageable, list.size());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userCouponRepository.getUserCoupons(anyLong(), any())).thenReturn(userCouponGetResponsesPage);

        Page<UserCouponGetResponseForMyPage> actualPage = userCouponService.getUserCoupons(1L, pageable);
        List<UserCouponGetResponseForMyPage> actualList = actualPage.getContent();

        assertThat(actualPage).isNotNull();
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getUserCouponId()).isNotNull().isEqualTo(fourthUserCoupon.getId());
        assertThat(actualList.get(0).getRange()).isNotNull().isEqualTo("도서");
        assertThat(actualList.get(0).getTarget()).isNotNull().isEqualTo(fourthUserCoupon.getBookName());
        assertThat(actualList.get(1).getUserCouponId()).isNotNull().isEqualTo(fifthUserCoupon.getId());
        assertThat(actualList.get(1).getRange()).isNotNull().isEqualTo("카테고리");
        assertThat(actualList.get(1).getTarget()).isNotNull().isEqualTo(fifthUserCoupon.getCategoryName());
        assertThat(actualList.get(2).getUserCouponId()).isNotNull().isEqualTo(sixthUserCoupon.getId());
        assertThat(actualList.get(2).getRange()).isNotNull().isEqualTo("전체");
        assertThat(actualList.get(2).getTarget()).isNotNull().isEqualTo("전체 도서");
        assertThat(actualPage.getTotalElements()).isEqualTo(userCouponGetResponsesPage.getTotalElements());
    }

    @Test
    @DisplayName("회원 쿠폰 조회 - 존재하지 않는 회원 아이디")
    void givenNotExistsUserId_whenGetUserCoupons_thenThrowUserNotExistException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        Pageable pageable = PageRequest.of(0, 2);
        assertThrows(UserNotExistException.class, () -> userCouponService.getUserCoupons(1L, pageable));
        verify(userCouponRepository, times(0)).getUserCoupons(anyLong(), any());
    }

    @Test
    @DisplayName("적용 가능한 회원 쿠폰 조회")
    void givenUserIdAndBookId_whenGetUsableUserCouponsByBookId_thenReturnListOfUserCouponGetResponseForOrder() {
        List<UserCouponGetResponseForOrderQuerydsl> categoryCouponList = new ArrayList<>();
        UserCouponGetResponseForOrderQuerydsl firstCategoryCoupon =
                new UserCouponGetResponseForOrderQuerydsl(
                        1L,
                        "firstCategoryCoupon",
                        0,
                        null,
                        10000,
                        50,
                        true,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );
        UserCouponGetResponseForOrderQuerydsl secondCategoryCoupon =
                new UserCouponGetResponseForOrderQuerydsl(
                        2L,
                        "secondCategoryCoupon",
                        3500,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );
        categoryCouponList.add(firstCategoryCoupon);
        categoryCouponList.add(secondCategoryCoupon);
        List<UserCouponGetResponseForOrderQuerydsl> bookCouponList = new ArrayList<>();
        UserCouponGetResponseForOrderQuerydsl firstBookCoupon =
                new UserCouponGetResponseForOrderQuerydsl(
                        3L,
                        "firstBookCoupon",
                        0,
                        null,
                        10000,
                        50,
                        true,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );
        UserCouponGetResponseForOrderQuerydsl secondBookCoupon =
                new UserCouponGetResponseForOrderQuerydsl(
                        4L,
                        "secondBookCoupon",
                        3500,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );
        bookCouponList.add(firstBookCoupon);
        bookCouponList.add(secondBookCoupon);

        when(userCouponRepository.getUsableUserCategoryCouponsByBookId(anyLong(), anyLong())).thenReturn(
                categoryCouponList);
        when(userCouponRepository.getUsableUserBookCouponsByBookId(anyLong(), anyLong())).thenReturn(bookCouponList);

        List<UserCouponGetResponseForOrder> actualList = userCouponService.getUsableUserCouponsByBookId(1L, 2L);

        assertThat(actualList).isNotNull().hasSize(categoryCouponList.size() + bookCouponList.size());
        assertThat(actualList.get(0).getUserCouponId()).isEqualTo(firstBookCoupon.getUserCouponId());
        assertThat(actualList.get(1).getUserCouponId()).isEqualTo(secondBookCoupon.getUserCouponId());
        assertThat(actualList.get(2).getUserCouponId()).isEqualTo(firstCategoryCoupon.getUserCouponId());
        assertThat(actualList.get(3).getUserCouponId()).isEqualTo(secondCategoryCoupon.getUserCouponId());
        verify(userCouponRepository, times(1)).getUsableUserCategoryCouponsByBookId(1L, 2L);
        verify(userCouponRepository, times(1)).getUsableUserBookCouponsByBookId(1L, 2L);
    }

    @Test
    @DisplayName("적용 가능한 회원 쿠폰 조회 - 적용 가능한 쿠폰이 없는 경우")
    void givenUserIdAndBookIdThatHasNoApplicableUserCoupon_whenGetUsableUserCouponsByBookId() {
        when(userCouponRepository.getUsableUserBookCouponsByBookId(anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(userCouponRepository.getUsableUserCategoryCouponsByBookId(anyLong(), anyLong())).thenReturn(
                new ArrayList<>());

        List<UserCouponGetResponseForOrder> actualList = userCouponService.getUsableUserCouponsByBookId(1L, 1L);

        assertThat(actualList).isNotNull().isEmpty();
        verify(userCouponRepository, times(1)).getUsableUserCategoryCouponsByBookId(1L, 1L);
        verify(userCouponRepository, times(1)).getUsableUserBookCouponsByBookId(1L, 1L);
    }

    @Test
    @DisplayName("적용 가능한 회원 쿠폰 조회(전체쿠폰)")
    void givenUserId_whenGetUsableTotalCoupons_thenReturnListOfUserCouponGetResponseForOrder() {
        List<UserCouponGetResponseForOrderQuerydsl> totalCouponList = new ArrayList<>();
        UserCouponGetResponseForOrderQuerydsl firstTotalCoupon =
                new UserCouponGetResponseForOrderQuerydsl(
                        1L,
                        "firstTotalCoupon",
                        0,
                        null,
                        10000,
                        50,
                        true,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );
        UserCouponGetResponseForOrderQuerydsl secondTotalCoupon =
                new UserCouponGetResponseForOrderQuerydsl(
                        2L,
                        "secondTotalCoupon",
                        3500,
                        1000,
                        null,
                        null,
                        false,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2)
                );
        totalCouponList.add(firstTotalCoupon);
        totalCouponList.add(secondTotalCoupon);

        when(userCouponRepository.getUsableTotalCoupons(anyLong())).thenReturn(totalCouponList);

        List<UserCouponGetResponseForOrder> actualList = userCouponService.getUsableTotalCoupons(1L);
        assertThat(actualList).isNotNull().hasSize(totalCouponList.size());
        assertThat(actualList.get(0).getUserCouponId()).isEqualTo(firstTotalCoupon.getUserCouponId());
        assertThat(actualList.get(1).getUserCouponId()).isEqualTo(secondTotalCoupon.getUserCouponId());
        verify(userCouponRepository, times(1)).getUsableTotalCoupons(1L);
    }

    @Test
    @DisplayName("회원 쿠폰 생성")
    void givenUserCouponCreateRequest_whenCreateUserCoupon_thenCreateUserCoupon() {
        UserGradeName bronze = new UserGradeName("브론즈");
        UserStatus userSTatus = new UserStatus("활동중");
        UserGrade userGrade = new UserGrade(
                1,
                bronze,
                10000,
                20000,
                1,
                LocalDate.now(),
                true);
        User user = new User(
                "test@naver.com",
                LocalDate.of(2020, 12, 22),
                "testPassword",
                "010-1111-2222",
                false,
                "test",
                userSTatus,
                userGrade,null
        );
        ReflectionTestUtils.setField(user, "id", 1L);
        Coupon coupon = new Coupon(
                "전체쿠폰",
                null,
                null,
                0,
                10000,
                null,
                null,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5),
                false,
                true
        );
        ReflectionTestUtils.setField(coupon, "id", 1L);

        UserCouponCreateRequest userCouponCreateRequest = new UserCouponCreateRequest();
        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", 1L);
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", 1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(couponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(userCouponRepository.save(any())).thenReturn(new UserCoupon(user, coupon));
        userCouponService.createUserCoupon(userCouponCreateRequest);
        verify(userRepository, times(1)).findById(user.getId());
        verify(couponRepository, times(1)).findById(coupon.getId());
        verify(userCouponRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원 쿠폰 생성 실패 - 존재하지 않는 회원 아이디")
    void givenNotExistsUserId_whenCreateUserCoupon_thenThrowUserNotExistException() {
        UserCouponCreateRequest userCouponCreateRequest = new UserCouponCreateRequest();
        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", 1L);
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", 1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotExistException.class, () -> userCouponService.createUserCoupon(userCouponCreateRequest));
        verify(userRepository, times(1)).findById(anyLong());
        verify(couponRepository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 쿠폰 생성 실패 - 존재하지 않는 도서 아이디")
    void givenNotExistsBookId_whenCreateUserCoupon_thenThrowBookNotExistException() {
        UserGradeName bronze = new UserGradeName("브론즈");
        UserStatus userSTatus = new UserStatus("활동중");
        UserGrade userGrade = new UserGrade(
                1,
                bronze,
                10000,
                20000,
                1,
                LocalDate.now(),
                true);
        User user = new User(
                "test@naver.com",
                LocalDate.of(2020, 12, 22),
                "testPassword",
                "010-1111-2222",
                false,
                "test",
                userSTatus,
                userGrade,null
        );
        ReflectionTestUtils.setField(user, "id", 1L);
        UserCouponCreateRequest userCouponCreateRequest = new UserCouponCreateRequest();
        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", user.getId());
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", 1L);
        when(userRepository.findById(userCouponCreateRequest.getUserId())).thenReturn(Optional.of(user));
        when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CouponNotExistsException.class, () -> userCouponService.createUserCoupon(userCouponCreateRequest));
        verify(userRepository, times(1)).findById(anyLong());
        verify(couponRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("쿠폰 사용")
    void givenUserCouponId_whenUseUserCoupon_thenUseUserCoupon() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 2L);
        Coupon coupon = new Coupon();
        ReflectionTestUtils.setField(coupon, "id", 3L);
        UserCoupon userCoupon = new UserCoupon(user, coupon);
        ReflectionTestUtils.setField(userCoupon, "id", 1L);
        when(userCouponRepository.findById(userCoupon.getId())).thenReturn(Optional.of(userCoupon));
        userCouponService.useUserCoupon(1L);
        assertThat(userCoupon.getDate()).isEqualTo(LocalDate.now());
        assertThat(userCoupon.getIsUsed()).isTrue();
    }

    @Test
    @DisplayName("쿠폰 사용 실패 - 존재하지 않는 회원 쿠폰 아이디")
    void givenNotExistsUserCouponId_whenUseUserCoupon_thenThrowUserCouponNotExistsException() {
        when(userCouponRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserCouponNotExistsException.class, () -> userCouponService.useUserCoupon(1L));
    }

    @Test
    @DisplayName("쿠폰 되돌려주기")
    void givenUserCouponId_whenGiveBackUserCoupon_thenGiveBackUserCoupon() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 2L);
        Coupon coupon = new Coupon();
        ReflectionTestUtils.setField(coupon, "id", 3L);
        UserCoupon userCoupon = new UserCoupon(user, coupon);
        ReflectionTestUtils.setField(userCoupon, "id", 1L);
        ReflectionTestUtils.setField(userCoupon, "date", LocalDate.now());
        ReflectionTestUtils.setField(userCoupon, "isUsed", true);
        when(userCouponRepository.findById(userCoupon.getId())).thenReturn(Optional.of(userCoupon));
        userCouponService.giveBackUserCoupon(userCoupon.getId());
        assertThat(userCoupon.getCreatedDate()).isEqualTo(LocalDate.now());
        assertThat(userCoupon.getIsUsed()).isFalse();
    }

    @Test
    @DisplayName("쿠폰 되돌려주기 실패 - 존재하지 않는 회원 쿠폰 아이디")
    void givenNotExistsUserCouponId_whenGiveBackUserCoupon_thenGiveBackUserCoupon() {
        when(userCouponRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserCouponNotExistsException.class, () -> userCouponService.giveBackUserCoupon(1L));
    }
}