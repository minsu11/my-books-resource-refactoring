package store.mybooks.resource.cart_item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.front.cart.domain.CartDetail;
import store.mybooks.resource.cart_item.dto.CartUserRedisKeyNameRequest;
import store.mybooks.resource.cart_item.service.CartItemService;
import store.mybooks.resource.config.HeaderProperties;

/**
 * packageName    : store.mybooks.resource.cart_item.controller <br/>
 * fileName       : CartItemControllerTest<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/24/24        Fiat_lux       최초 생성<br/>
 */
@WebMvcTest(value = CartItemController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }


    @Test
    @DisplayName("mysql에서 장바구니 데이터를 redis로 이동")
    void moveDataMysqlToRedisTest() throws Exception {
        Long userId = 1L;
        CartUserRedisKeyNameRequest cartUserRedisKeyNameRequest = new CartUserRedisKeyNameRequest("cartKey");
        CartDetail cartDetail1 = new CartDetail(1L, 1, "name", "image", 123, 123, 123, "status");
        CartDetail cartDetail2 = new CartDetail(2L, 1, "name", "image", 123, 123, 123, "status");
        List<CartDetail> cartDetailList = new ArrayList<>();
        cartDetailList.add(cartDetail1);
        cartDetailList.add(cartDetail2);

        when(cartItemService.registerMysqlToRedis(any(Long.class), any(CartUserRedisKeyNameRequest.class))).thenReturn(
                cartDetailList);

        mockMvc.perform(post("/api/carts/get/items")
                        .header(HeaderProperties.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cartUserRedisKeyNameRequest)))
                .andExpect(status().isCreated())
                .andDo(document("move-cartData-mysql-to-redis",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("cartKey").description("redis cart key")
                        ),
                        responseFields(
                                fieldWithPath("[].bookId").description("책 id"),
                                fieldWithPath("[].cartDetailAmount").description("물건 수량"),
                                fieldWithPath("[].name").description("상품 이름"),
                                fieldWithPath("[].bookImage").description("상품 이미지"),
                                fieldWithPath("[].cost").description("상품 정가"),
                                fieldWithPath("[].saleCost").description("상품 할인가"),
                                fieldWithPath("[].stock").description("상품 재고"),
                                fieldWithPath("[].sellingStatus").description("상품 판매 상태")
                        )));
    }


}