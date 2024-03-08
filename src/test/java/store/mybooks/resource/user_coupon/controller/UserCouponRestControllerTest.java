package store.mybooks.resource.user_coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.user_coupon.dto.response.UserCouponGetResponseForMyPage;
import store.mybooks.resource.user_coupon.service.UserCouponService;

/**
 * packageName    : store.mybooks.resource.user_coupon.controller
 * fileName       : UserCouponRestControllerTest
 * author         : damho-lee
 * date           : 3/8/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/24          damho-lee          최초 생성
 */
@WebMvcTest(value = UserCouponRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserCouponRestControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserCouponService userCouponService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("회원 쿠폰 페이지 요청")
    void givenPageableAndUserId_whenGetUserCoupons_thenReturnPageOfUserCoupon() throws Exception {
        List<UserCouponGetResponseForMyPage> userCouponList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 2);
        UserCouponGetResponseForMyPage firstUserCoupon = new UserCouponGetResponseForMyPage(
                1L,
                "firstUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        UserCouponGetResponseForMyPage secondUserCoupon = new UserCouponGetResponseForMyPage(
                2L,
                "secondUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        UserCouponGetResponseForMyPage thirdUserCoupon = new UserCouponGetResponseForMyPage(
                3L,
                "thirdUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        UserCouponGetResponseForMyPage fourthUserCoupon = new UserCouponGetResponseForMyPage(
                4L,
                "fourthUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );

        userCouponList.add(firstUserCoupon);
        userCouponList.add(secondUserCoupon);
        userCouponList.add(thirdUserCoupon);
        userCouponList.add(fourthUserCoupon);
        int offset = (int) pageable.getOffset();
        Page<UserCouponGetResponseForMyPage> userCouponPage = new PageImpl<>(
                userCouponList.subList(offset, offset + pageable.getPageSize()),
                pageable,
                userCouponList.size()
        );
        when(userCouponService.getUserCoupons(1L, any())).thenReturn(userCouponPage);

        mockMvc.perform(
                        get("/api/user-coupon/page?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize())
                                .header("X-USER-ID", 1)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(thirdUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$.content[1].id").value(fourthUserCoupon.getUserCouponId()));

    }
}



















