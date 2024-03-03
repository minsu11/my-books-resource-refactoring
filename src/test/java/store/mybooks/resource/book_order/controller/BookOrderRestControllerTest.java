package store.mybooks.resource.book_order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import store.mybooks.resource.book_order.dto.response.BookOrderUserResponse;
import store.mybooks.resource.book_order.service.BookOrderService;

/**
 * packageName    : store.mybooks.resource.book_order.controller<br>
 * fileName       : BookOrderRestControllerTest<br>
 * author         : minsu11<br>
 * date           : 3/3/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/3/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(BookOrderRestController.class)
class BookOrderRestControllerTest {
    @MockBean
    private BookOrderService bookOrderService;

    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext,
//               RestDocumentationContextProvider restDocumentationContextProvider) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentationContextProvider))
//                .build();
//    }

    @Test
    @DisplayName("주문 내역")
    void getBookOrderPageById() throws Exception {
        BookOrderUserResponse bookOrderUserResponse = getBookOrderUserResponse();

        List<BookOrderUserResponse> bookOrderUserResponses =
                List.of(bookOrderUserResponse);
        Pageable pageable = PageRequest.of(0, 2);

        Page<BookOrderUserResponse> bookOrderPage =
                new PageImpl<>(bookOrderUserResponses, pageable, bookOrderUserResponses.size());

        when(bookOrderService.getBookOrderResponseList(any(), any())).thenReturn(bookOrderPage);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(1L))
                .andExpect(jsonPath("$.content[0].statusId").value("test"))
                .andExpect(jsonPath("$.content[0].deliveryRuleId").value(1))
                .andExpect(jsonPath("$.content[0].wrapId").value(1))
                .andExpect(jsonPath("$.content[0].date").value("1212-12-12"))
                .andExpect(jsonPath("$.content[0].invoiceNumber").value("test"))
                .andExpect(jsonPath("$.content[0].receiverName").value("testName"))
                .andExpect(jsonPath("$.content[0].receiverAddress").value("testAddress"))
                .andExpect(jsonPath("$.content[0].receiverPhoneNumber").value("010-0000-0000"))
                .andExpect(jsonPath("$.content[0].receiverMessage").value("testMessage"))
                .andExpect(jsonPath("$.content[0].totalCost").value(1000))
                .andExpect(jsonPath("$.content[0].pointCost").value(100))
                .andExpect(jsonPath("$.content[0].couponCost").value(100))
                .andExpect(jsonPath("$.content[0].number").value("123123123"))
                .andExpect(jsonPath("$.content[0].findPassword").value(""));

    }

    private static BookOrderUserResponse getBookOrderUserResponse() {
        BookOrderUserResponse bookOrderUserResponse = new BookOrderUserResponse();
        bookOrderUserResponse.setUserId(1L);
        bookOrderUserResponse.setStatusId("test");
        bookOrderUserResponse.setDeliveryRuleId(1);
        bookOrderUserResponse.setWrapId(1);
        bookOrderUserResponse.setDate(LocalDate.of(1212, 12, 12));
        bookOrderUserResponse.setInvoiceNumber("test");
        bookOrderUserResponse.setReceiverName("testName");
        bookOrderUserResponse.setReceiverAddress("testAddress");
        bookOrderUserResponse.setReceiverPhoneNumber("010-0000-0000");
        bookOrderUserResponse.setReceiverMessage("testMessage");
        bookOrderUserResponse.setTotalCost(1000);
        bookOrderUserResponse.setPointCost(100);
        bookOrderUserResponse.setCouponCost(100);
        bookOrderUserResponse.setNumber("123123123");
        bookOrderUserResponse.setFindPassword("");
        return bookOrderUserResponse;
    }

    @Test
    void getBookOrderPageByStatusId() {
    }
}