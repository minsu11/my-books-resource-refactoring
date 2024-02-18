package store.mybooks.resource.orders_status.controller;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusCreateRequest;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusCreateResponse;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.exception.OrdersStatusAlreadyExistException;
import store.mybooks.resource.orders_status.exception.OrdersStatusNotFoundException;
import store.mybooks.resource.orders_status.service.OrdersStatusService;

/**
 * packageName    : store.mybooks.resource.orders_status.controller
 * fileName       : OrdersStatusControllerTest
 * author         : minsu11
 * date           : 2/16/24
 * description    : OrdersStatus 관련 unit test code
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OrdersStatusControllerUnitTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrdersStatusService ordersStatusService;

    @Test
    void givenOrdersStatus_whenGetOrderStatusById_thenReturnOrdersStatusResponse() throws Exception {
        OrdersStatusResponse response = new OrdersStatusResponse("test");
        given(ordersStatusService.getOrdersStatusById(any())).willReturn(response);

        mockMvc.perform(get("/api/orders-statuses/{orderStatusId}", "test")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo("test")));
    }

    @Test
    void givenOrderStatus_whenGetOrderStatusById_thenReturnOrdersStatusNotFoundException() throws Exception {
        given(ordersStatusService.getOrdersStatusById(anyString())).willThrow(OrdersStatusNotFoundException.class);

        Throwable th = catchThrowable(() ->
                mockMvc.perform(get("/api/orders-statuses/{id}", "test1"))
                        .andExpect(status().isBadRequest())
                        .andDo(print()));
        Assertions.assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(OrdersStatusNotFoundException.class);

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

    @Test
    void givenOrdersStatus_whenGetOrdersStatusList_thenReturnOrdersStatusNotFoundException() throws Exception {
        given(ordersStatusService.getOrdersStatusList()).willThrow(NullPointerException.class);

        Throwable th = catchThrowable(() ->
                mockMvc.perform(get("/api/orders-statuses"))
                        .andExpect(status().isBadRequest())
                        .andDo(print()));
        Assertions.assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void givenOrdersStatus_whenCreateOrdersStatus_thenReturnOrdersStatusCreateResponse() throws Exception {
        OrdersStatusCreateResponse response = new OrdersStatusCreateResponse("test");
        OrdersStatusCreateRequest request = new OrdersStatusCreateRequest();
        ObjectMapper mapper = new ObjectMapper();
        given(ordersStatusService.createOrdersStatus(any())).willReturn(response);

        mockMvc.perform(post("/api/orders-statuses")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("test")));
    }

    @Test
    void givenOrderStatus_whenCreateOrderStatus_thenReturnOrders() throws Exception {
        OrdersStatusCreateRequest request = new OrdersStatusCreateRequest();
        ObjectMapper mapper = new ObjectMapper();
        given(ordersStatusService.createOrdersStatus(any())).willThrow(OrdersStatusAlreadyExistException.class);
        Throwable th = catchThrowable(() ->
                mockMvc.perform(post("/api/orders-statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print()));
        Assertions.assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(OrdersStatusAlreadyExistException.class);
    }
}