package store.mybooks.resource.orders_status.controller;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.service.OrdersStatusService;

/**
 * packageName    : store.mybooks.resource.orders_status.controller
 * fileName       : OrdersStatusControllerTest
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OrdersStatusControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrdersStatusService ordersStatusService;

    @Test
    void givenOrdersStatus_whenGetOrderStatusById_thenReturnOrdersStatusResponse() throws Exception {
        OrdersStatusResponse response = new OrdersStatusResponse("test");
        given(ordersStatusService.getOrdersStatusById(anyString())).willReturn(response);

        mockMvc.perform(get("/api/orders-statuses/{id}")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo("test")));
    }

    @Test
    void name() {

    }

    @Test
    void givenOrdersStatus_whenGetOrdersStatusList_thenReturnOrdersStatusResponseList() throws Exception {
        given(ordersStatusService.getOrdersStatusList()).willReturn(
                List.of(new OrdersStatusResponse("test")));
        mockMvc.perform(get("/api/orders-statuses")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo("test")));
    }
}